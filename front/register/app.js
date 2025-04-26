document.getElementById("register-btn").addEventListener("click", function () {
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();
  const email = document.getElementById("email").value.trim();

  if (!username || !password || !email) {
    alert("모든 항목을 입력해주세요.");
    return;
  }

  fetch("http://localhost:8080/api/auth/signup", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ username, password, email })
  })
      .then(response => {
        if (!response.ok) throw new Error("회원가입 실패");
        return response.json();
      })
      .then(data => {
        alert("회원가입이 완료되었습니다!");
        location.href = "../login/index.html"; // 로그인 화면으로 이동
      })
      .catch(error => {
        console.error("회원가입 에러:", error);
        alert("회원가입 중 오류가 발생했습니다.");
      });
});
