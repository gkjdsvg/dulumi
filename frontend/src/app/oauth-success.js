const response = await fetch("/loginForm", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
});

const data = await response.json();

localStorage.setItem("access_token", data.accessToken);
localStorage.setItem("refresh_token", data.refreshToken);