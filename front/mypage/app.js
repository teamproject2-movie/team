// 페이지 로딩 시 현재 프로필 이미지 불러오기
document.addEventListener("DOMContentLoaded", () => {
    fetch(" ", { 
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`
      }
    })
    .then(res => res.json())
    .then(data => {
      const profile = data.profileImage || "default.png";
      document.getElementById("currentProfile").src = profile;
    })
    .catch(err => {
      console.error(err);
      alert("프로필 이미지를 불러오지 못했습니다.");
    });
  });
  
  // 프로필 사진 목록 toggle
function toggleProfileSelection() {
    const box = document.getElementById("profileOptions");
    box.classList.toggle("hidden");
}
  
  // 프로필 선택
function selectProfile(filename) {
    fetch("  ", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`
      },
      body: JSON.stringify({ profileImage: filename })
    })
    .then(res => {
      if (!res.ok) throw new Error("프로필 저장 실패");
      document.getElementById("currentProfile").src = filename;
      alert("프로필 이미지가 변경되었습니다.");
    })
    .catch(err => {
      console.error(err);
      alert("프로필 변경 중 오류가 발생했습니다.");
    });
}
  
  // 이메일/비밀번호 변경
function updateInfo() {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
  
    fetch("http://localhost:8080/api/user/update", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`
      },
      body: JSON.stringify({ email, password })
    })
    .then(res => {
      if (!res.ok) throw new Error("정보 수정 실패");
      alert("정보가 수정되었습니다.");
    })
    .catch(err => {
      console.error(err);
      alert("정보 수정 중 오류가 발생했습니다.");
    });
}
  
  // 로그아웃
function logout() {
    localStorage.removeItem("accessToken");
    alert("로그아웃되었습니다.");
    location.href = "../login/index.html";
}
