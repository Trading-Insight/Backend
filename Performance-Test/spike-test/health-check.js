import http from 'k6/http';
import { check, sleep } from 'k6';

const baseUrl = __ENV.K6_TEST_URL;
const targetUrl = `${baseUrl.replace(/\/$/, '')}/health-check`;

// Spike Test Configuration
const SPIKE_VUS = Number(__ENV.K6_SPIKE_VUS || 10000);
const RAMP_UP = __ENV.K6_RAMP_UP || '10s';
const HOLD = __ENV.K6_HOLD || '30s';
const RAMP_DOWN = __ENV.K6_RAMP_DOWN || '20s';

console.log(`[Spike Test] 시작 - Spike VUs: ${SPIKE_VUS}, Ramp Up: ${RAMP_UP}, Hold: ${HOLD}, Ramp Down: ${RAMP_DOWN}, Target: ${targetUrl}`);

export const options = {
  scenarios: {
    spike: {
      executor: 'ramping-vus',
      stages: [
        { duration: RAMP_UP, target: SPIKE_VUS },    // Sudden spike up
        { duration: HOLD, target: SPIKE_VUS },       // Hold at peak
        { duration: RAMP_DOWN, target: 0 },          // Rapid decline
      ],
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<1000'],
    http_req_failed: ['rate<0.05'],
  },
};

export default function () {
  const res = http.get(targetUrl, { 
    tags: { 
      endpoint: 'health',
      test_type: 'spike'
    } 
  });
  
  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 1s': (r) => r.timings.duration < 1000,
  });
  
  sleep(1);
} 