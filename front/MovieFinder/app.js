function searchMovies() {
  const query = document.getElementById("searchInput").value.trim();
  const resultSection = document.getElementById("resultSection");

fetch(`http://54.252.242.219:8080/api/movies/search?query=${encodeURIComponent(query)}`, {
  method: "GET",
  headers: {
    "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
    "Content-Type": "application/json"
  }
})
.then(response => {
  if (!response.ok) throw new Error("검색 실패");
  return response.json();
})
.then(movies => {
  resultSection.innerHTML = `<h2>"${query}"에 대한 영화 목록</h2><p>총 ${movies.length}개의 영화가 검색되었습니다.</p>`;

  const cardContainer = document.createElement("div");
  cardContainer.className = "card-container";

  movies.forEach(movie => {
    const card = document.createElement("div");
    card.className = "movie-card";
    card.onclick = () => {
      window.location.href = `../movieDetail/index.html?id=${movie.id}`;
    };
    const genreTags = movie.genres.map(genre => `<span class="genre-tag">${genre}</span>`).join(" ");
    card.innerHTML = `
        <img src="${movie.posterUrl}" alt="포스터" />
        <h3>${movie.title}</h3>
        <div class="genre-box">${genreTags}</div>
    `;
    cardContainer.appendChild(card);
  });

  resultSection.appendChild(cardContainer);
})
.catch(error => {
  console.error("검색 에러:", error);
  alert("검색 결과가 없습니다.");
});
};
