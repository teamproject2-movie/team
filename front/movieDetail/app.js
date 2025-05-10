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
          setupBookmarkButton(data.id); //북마크 연결
          loadTrailerFromTMDB(data.id); //예고편 연결
      });
});

function setupBookmarkButton(movieId){
  const token = localStorage.getItem("accessToken");
  const bookmarkBtn = document.getElementById("bookmark-btn");

  //북마크 확인
  fetch(`http://54.252.242.219:8080/api/bookmarks/check/${movieId}`,{
    method:"GET",
    headers:{
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json"
    }
  })
  .then(res => res.json())
  .then(data => {
    bookmarkBtn.innerText = data.bookmarked ? "❤️" : "🤍";
  });

  //북마크 토글
  bookmarkBtn.addEventListener("click", () => {
  fetch("http://54.252.242.219:8080/api/bookmarks/toggle",{
    method:"POST",
    headers:{
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify({movieId: movieId})
  })
    .then(res => res.json())
    .then(data => {
      bookmarkBtn.innerText = data.bookmarked ? "❤️" : "🤍";
    })
    .catch(err => console.error("북마크 처리 실패!"))
  });
}

function loadTrailerFromTMDB(tmdbId) {
  const TMDB_API_KEY = "75cfcf8f8d40cd52cf539e3f1aa7e837";

  fetch(`https://api.themoviedb.org/3/movie/${tmdbId}/videos?api_key=${TMDB_API_KEY}`)
    .then(res => res.json())
    .then(data => {
      const trailer = data.results.find(
        v => v.site === "YouTube" && v.type === "Trailer"
      );

      if (trailer) {
        const container = document.getElementById("trailer-container");
        container.innerHTML = `
          <h3>예고편</h3>
          <iframe width="100%" height="340"
            src="https://www.youtube.com/embed/${trailer.key}"
            frameborder="0"
            allowfullscreen>
          </iframe>
        `;
      } else {
        console.log("예고편이 없습니다.");
      }
    })
}