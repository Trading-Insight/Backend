import http from 'k6/http';
import { check, sleep } from 'k6';

const baseUrl = __ENV.K6_TEST_URL;
const targetUrl = `${baseUrl.replace(/\/$/, '')}/health-check`;

console.log(`[k6] 테스트 시작!!. Target URL: ${targetUrl}`);

export const options = {
  thresholds: {
    http_req_duration: ['p(95)<300'],
    http_req_failed: ['rate<0.01'],
  },
};

export default function () {
  const res = http.get(targetUrl, { tags: { endpoint: 'health' } });
  check(res, {
    'status is 200': (r) => r.status === 200,
  });
  sleep(1);
} 