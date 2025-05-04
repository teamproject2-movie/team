/*임시 데이터
const dummyData = [
    {
      id: 550,
      title: "파이트 클럽",
      posterUrl: "https://image.tmdb.org/t/p/w500/bptfVGEQuv6vDTIMVCHjJ9Dz8PX.jpg",
      overview: "에드워드는 삶에 지쳐있다. 우연히 만난 타일러는 파이트 클럽이라는 비밀 조직을 만들고 두 사람은 거침없이 자신들의 분노를 표출한다.",
      releaseDate: "1999-10-15",
      genres: ["드라마"]
    },
    {
      id: 603,
      title: "매트릭스",
      posterUrl: "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
      overview: "평범한 회사원인 네오는 어느 날 진실을 알게 되고, 인류를 구할 전쟁에 휘말린다.",
      releaseDate: "1999-03-30",
      genres: ["액션", "SF"]
    },
    {
      id: 680,
      title: "펄프 픽션",
      posterUrl: "https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
      overview: "마피아, 복수, 우연... 일상이 꼬여버린 LA의 인물들이 전개하는 블랙 코미디 범죄극.",
      releaseDate: "1994-10-14",
      genres: ["범죄", "드라마"]
    },
    {
      id: 157336,
      title: "인터스텔라",
      posterUrl: "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
      overview: "지구의 마지막을 앞두고 인류를 구원하기 위한 우주 탐사가 시작된다. 아버지 쿠퍼와 딸 머피의 이야기.",
      releaseDate: "2014-11-05",
      genres: ["모험", "드라마", "SF"]
    },
    {
      id: 27205,
      title: "인셉션",
      posterUrl: "https://image.tmdb.org/t/p/w500/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg",
      overview: "꿈속의 꿈 속으로 들어가 타인의 무의식을 조작하는 도미닉 코브의 팀이 벌이는 마지막 미션.",
      releaseDate: "2010-07-15",
      genres: ["액션", "SF", "스릴러"]
    }
];
  
// 상세 페이지
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!로컬 데이터 부분!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const movieId = params.get("id");
  
    if (!movieId) {
      alert("잘못된 접근입니다. 영화 ID가 없습니다.");
      return;
    }
    const data = dummyData.find(movie => movie.id === parseInt(movieId));
    if (!data) {
        alert("해당 영화를 찾을수 없습니다.")   
        return;
    }

    document.getElementById("movie-title").innerText = data.title;
    document.getElementById("movie-poster").src = data.posterUrl;
    document.getElementById("release-date").innerText = data.releaseDate;
    document.getElementById("movie-overview").innerText = data.overview || "줄거리 정보가 없습니다.";
    const genreContainer = document.getElementById("movie-genre");
    genreContainer.innerHTML = "";
    data.genres.forEach(genre => {
        const tag = document.createElement("span");
        tag.className = "genre-tag"; // 또는 그대로 "tag-box" 안에 있어도 됨
        tag.innerText = genre;
        genreContainer.appendChild(tag);
});

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */


document.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);
  const movieId = params.get("id");

  if (!movieId) {
      alert("잘못된 접근입니다.");
      return;
  }

  const token = localStorage.getItem("accessToken");
  if (!token) {
      alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
      window.location.href = "../login/index.html";
      return;
  }

  fetch(`http://54.252.242.219:8080/api/movies/search/${movieId}`, {
      method: "GET",
      headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
      }
  })
      .then(res => {
          if (!res.ok) throw new Error("영화 정보를 불러오지 못했습니다.");
          return res.json();
      })
      .then(data => {
          document.getElementById("movie-title").innerText = data.title;
          document.getElementById("movie-poster").src = data.posterUrl;
          document.getElementById("release-date").innerText = data.releaseDate;
          document.getElementById("movie-overview").innerText = data.overview || "줄거리 정보 없음";

          const genreContainer = document.getElementById("movie-genre");
          genreContainer.innerHTML = "";
          data.genres.forEach(g => {
              const span = document.createElement("span");
              span.className = "genre-tag";
              span.innerText = g;
              genreContainer.appendChild(span);
          });

          setupBookmarkButton(data.id);
      })
      .catch(err => {
          console.error("Error:", err);
          alert("상세 정보 로딩 실패");
      });
});
