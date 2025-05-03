document.getElementById("login-btn").addEventListener("click", function () {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    if (!username || !password) {
        alert("아이디와 비밀번호를 모두 입력하세요.");
        return;
    }

    fetch("http://54.252.242.219:8080/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    })
        .then(async response => {
            const text = await response.text();

            if (!response.ok) {
                // 서버에서 에러 메시지 왔으면 그대로 띄워
                throw new Error(text);
            }

            // 서버가 준 토큰 처리
            const pureToken = text.replace("Bearer ", "");
            localStorage.setItem("accessToken", pureToken);
            alert("로그인 성공!");
            location.href = "../MovieFinder/index.html"; // 로그인 성공 시 메인 페이지 이동
        })
        .catch(error => {
            console.error("로그인 에러:", error);
            alert(error.message); // 서버가 보낸 에러메시지 그대로 띄움
        });
});
