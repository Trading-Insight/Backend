import http from 'k6/http';
import { check, sleep } from 'k6';

const baseUrl = __ENV.K6_TEST_URL;
const targetUrl = `${baseUrl.replace(/\/$/, '')}/health-check`;

// VU 기반 테스트 설정
const TARGET_VUS = Number(__ENV.K6_TARGET_VUS || 1000);
const TEST_DURATION = __ENV.K6_TEST_DURATION || '30s';
const RAMP_UP_TIME = __ENV.K6_RAMP_UP_TIME || '10s';
const TARGET_P95 = Number(__ENV.K6_TARGET_P95 || 200);
const MAX_ERROR_RATE = Number(__ENV.K6_MAX_ERROR_RATE || 0.01);

export const options = {
  scenarios: {
    'VU-Test': {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: RAMP_UP_TIME, target: TARGET_VUS },  // RAMP_UP_TIME 걸쳐 TARGET_VUS VU까지 증가
        { duration: TEST_DURATION, target: TARGET_VUS }, // TEST_DURATION동안 TARGET_VUS VU 유지
        { duration: '10s', target: 0 },                  // 10초에 걸쳐 0으로 감소
      ],
    },
  },
  thresholds: {
    'http_req_duration': [`p(95)<${TARGET_P95}`],
    'http_req_failed': [`rate<${MAX_ERROR_RATE}`],
  },
};

export function setup() {
  console.log(`[부하 테스트 시작]`);
  console.log(`[목표] ${TARGET_VUS} VUs (각 VU가 1초에 1회 요청) = ${TARGET_VUS} RPS`);
  console.log(`[목표 성능] p95 < ${TARGET_P95}ms, 에러율 < ${MAX_ERROR_RATE * 100}%`);
  console.log(`[타겟] ${targetUrl}`);
  console.log(`[테스트 구성]`);
  console.log(`  - Ramp Up: ${RAMP_UP_TIME} (0 → ${TARGET_VUS} VUs)`);
  console.log(`  - 유지: ${TEST_DURATION} (${TARGET_VUS} VUs)`);
  console.log(`  - Ramp Down: 10s (${TARGET_VUS} → 0 VUs)`);
  console.log(`  - 각 VU: 1초에 1회 요청`);
  return { startTime: new Date() };
}

export default function () {
  const res = http.get(targetUrl, {
    timeout: '10s',
    headers: {
      'Connection': 'keep-alive',
      'Accept': 'application/json',
      'User-Agent': 'k6-load-test',
    },
  });

  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500,
  });

  // 각 VU가 1초에 한 번씩 요청하도록 1초 대기
  sleep(1);
}