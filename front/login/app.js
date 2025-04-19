document.getElementById("login-btn").addEventListener("click", function () {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
  
    if (!email || !password) {
      alert("아이디와 비밀번호를 모두 입력하세요.");
      return;
    }
  
    fetch("http://52.64.175.1:8080/api/user/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email, password })
    })
      .then(response => {
        if (!response.ok) throw new Error("로그인 실패");
        return response.json();
      })
      .then(data => {
        const token = data.data;
        if (!token) throw new Error("인증 실패");
  
        localStorage.setItem("accessToken", token);
        alert("로그인 성공!");
        location.href = "Todo_List/index.html";
      })
      .catch(error => {
        console.error("로그인 에러:", error);
        alert("아이디 또는 비밀번호가 틀렸습니다.");
      });
  });
  