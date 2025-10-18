import http from 'k6/http';
import { check, sleep } from 'k6';

const baseUrl = __ENV.K6_TEST_URL;
const targetUrl = `${baseUrl.replace(/\/$/, '')}/health-check`;

// 지속성 테스트 설정
const TARGET_VUS = Number(__ENV.K6_TARGET_VUS || 1000);
const TEST_DURATION = __ENV.K6_TEST_DURATION || '1h';  // 기본 1시간
const RAMP_UP_TIME = __ENV.K6_RAMP_UP_TIME || '300s';
const TARGET_P95 = Number(__ENV.K6_TARGET_P95 || 500);
const MAX_ERROR_RATE = Number(__ENV.K6_MAX_ERROR_RATE || 0.01);

export const options = {
  scenarios: {
    'Endurance-Test': {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: RAMP_UP_TIME, target: TARGET_VUS },  // 300초에 걸쳐 TARGET_VUS VU까지 증가
        { duration: TEST_DURATION, target: TARGET_VUS }, // 1시간 동안 TARGET_VUS VU 유지
        { duration: '30s', target: 0 },                  // 30초에 걸쳐 0으로 감소
      ],
    },
  },
  thresholds: {
    'http_req_duration': [`p(95)<${TARGET_P95}`],
    'http_req_failed': [`rate<${MAX_ERROR_RATE}`],
    'http_req_duration{status:200}': [`p(99)<${TARGET_P95 * 2}`], // 99퍼센타일도 모니터링
    'http_reqs': [`rate>${TARGET_VUS * 0.9}`], // RPS가 목표의 90% 이상 유지되어야 함
  },
};

export function setup() {
  console.log(`[지속성 테스트 시작]`);
  console.log(`[목표] ${TARGET_VUS} VUs (각 VU가 1초에 1회 요청) = ${TARGET_VUS} RPS`);
  console.log(`[목표 성능] p95 < ${TARGET_P95}ms, 에러율 < ${MAX_ERROR_RATE * 100}%`);
  console.log(`[타겟] ${targetUrl}`);
  console.log(`[테스트 구성]`);
  console.log(`  - Ramp Up: ${RAMP_UP_TIME} (0 → ${TARGET_VUS} VUs)`);
  console.log(`  - 유지: ${TEST_DURATION} (${TARGET_VUS} VUs)`);
  console.log(`  - Ramp Down: 30s (${TARGET_VUS} → 0 VUs)`);
  console.log(`  - 각 VU: 1초에 1회 요청`);
  console.log(`  - 총 테스트 시간: ${RAMP_UP_TIME} + ${TEST_DURATION} + 30s`);
  return { startTime: new Date() };
}

export default function () {
  const res = http.get(targetUrl, {
    timeout: '10s',
    headers: {
      'Connection': 'keep-alive',
      'Accept': 'application/json',
      'User-Agent': 'k6-endurance-test',
    },
  });

  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500,
    'response time < 1s': (r) => r.timings.duration < 1000,
  });

  // 각 VU가 1초에 한 번씩 요청하도록 1초 대기
  sleep(1);
}

export function teardown(data) {
  const endTime = new Date();
  const duration = (endTime - data.startTime) / 1000; // 초 단위
  
  console.log(`[지속성 테스트 완료]`);
  console.log(`[총 테스트 시간] ${duration}초`);
  console.log(`[시작 시간] ${data.startTime.toISOString()}`);
  console.log(`[종료 시간] ${endTime.toISOString()}`);
} 