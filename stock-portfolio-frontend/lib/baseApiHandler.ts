export async function getJsonHandler(fullUrl: string, sessionId = null) {
  return await fetch(fullUrl, {
    headers: {
      Cookie: `sessionid=${sessionId}`,
    },
  }).then(async (r) => [r.status, await r.json()]);
}

async function sendJson(
  fullUrl: string,
  method: "PUT" | "POST",
  json: JSON,
  sessionId,
) {
  return await fetch(fullUrl, {
    method: method,
    body: JSON.stringify(json),
    headers: {
      "Content-Type": "application/json",
      Cookie: `sessionid=${sessionId}`,
    },
  }).then(async (r) => [r.status, await r.json()]);
}

export async function postJsonHandler(
  fullUrl: string,
  json: JSON,
  sessionId = null,
) {
  return await sendJson(fullUrl, "POST", json, sessionId);
}

export async function putJsonHandler(
  fullUrl: string,
  json: JSON,
  sessionId = null,
) {
  return await sendJson(fullUrl, "PUT", json, sessionId);
}

export async function deleteJsonHandler(fullUrl: string, sessionId = null) {
  return await fetch(fullUrl, {
    method: "DELETE",
    headers: {
      Cookie: `sessionid=${sessionId}`,
    },
  }).then(async (r) => [r.status, await r.json()]);
}
