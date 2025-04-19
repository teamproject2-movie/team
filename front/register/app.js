document.getElementById("register-btn").addEventListener("click", function () {
  const name = document.getElementById("name").value.trim();
  const accountID = document.getElementById("accountID").value.trim();
  const email = document.getElementById("email").value.trim();

  if (!name || !accountID || !email) {
    alert("모든 항목을 입력해주세요.");
    return;
  }

  fetch("http://52.64.175.1:8080/api/user/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ name, accountID, email })
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
