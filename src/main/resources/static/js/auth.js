// auth.js

// Lưu token vào localStorage
function saveToken(token) {
    localStorage.setItem("jwtToken", token);
}

// Lấy token
function getToken() {
    return localStorage.getItem("jwtToken");
}

// Gọi API có gắn token tự động
async function authFetch(url, options = {}) {
    const token = getToken();

    const headers = {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
        ...(options.headers || {}),
    };

    const config = {
        ...options,
        headers,
    };

    return fetch(url, config);
}
