let allMovies = [];
let bookmarkedIds = [];

//영화 검색 창
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
  allMovies = movies;
  loadBookmarkedIds().then(()=> {
    renderPage(1);
  })
  const cardContainer = document.createElement("div");
  cardContainer.className = "card-container";

  resultSection.appendChild(cardContainer);
})
.catch(error => {
  console.error("검색 에러:", error);
  alert("검색 결과가 없습니다.");
});
};

//북마크 표시
function loadBookmarkedIds(){
  const token = localStorage.getItem("accessToken");
  if (!token) return Promise.resolve(); //로그인 안 했으면 패스

  return fetch("http://54.252.242.219:8080/api/bookmarks/lists", {
    headers: {
      "Authorization" : `Bearer ${token}`
    }
  })
  .then(res => {
    if(!res.ok) return[];
    return res.json();
  })
  .then(bookmarks => {
    bookmarkedIds = bookmarks.map(b => b.movieId);
  })
  .catch(() => {
    bookmarkedIds = [];
  });
}

function renderPage(page) {
  const resultSection = document.getElementById("resultSection");
  const query = document.getElementById("searchInput").value.trim();
  const itemsPerPage = 8;
  const start = (page - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  const currentMovies = allMovies.slice(start, end);

  resultSection.innerHTML = `
    <h2>"${query}"에 대한 영화 목록</h2>
    <p>총 ${allMovies.length}개의 영화가 검색되었습니다.</p>
  `;

  const cardContainer = document.createElement("div");
  cardContainer.className = "card-container";

  currentMovies.forEach(movie => {
    const genreTags = movie.genres.map(genre => `<span class="genre-tag">${genre}</span>`).join(" ");
    const card = document.createElement("div");
    card.className = "movie-card";
    card.onclick = () => {
      window.location.href = `../movieDetail/index.html?id=${movie.id}`;
    };

    const isBookmarked = bookmarkedIds.includes(movie.id);
    const token = localStorage.getItem("accessToken");
    const heart = token ? (isBookmarked ? "❤️" : "🤍") : "";

    card.innerHTML = `
      <span class="bookmark-indicator">${heart}</span>
      <img src="${movie.posterUrl}" alt="포스터" />
      <h3>${movie.title}</h3>
      <div class="genre-box">${genreTags}</div>
    `;

    cardContainer.appendChild(card);
  });

  resultSection.appendChild(cardContainer);

  renderPagination(page);
}

//페이지 표시
function renderPagination(currentPage) {
  const pagination = document.createElement("div");
  pagination.className = "pagination";
  const itemsPerPage = 8;
  const totalPages = Math.ceil(allMovies.length / itemsPerPage);

  // 이전 버튼
  if (currentPage > 1) {
    const prevBtn = document.createElement("button");
    prevBtn.innerText = "◀ 이전";
    prevBtn.onclick = () => renderPage(currentPage - 1);
    pagination.appendChild(prevBtn);
  }

  // 페이지 숫자 버튼
  for (let i = 1; i <= totalPages; i++) {
    const btn = document.createElement("button");
    btn.innerText = i;
    if (i === currentPage) btn.classList.add("active");
    btn.onclick = () => renderPage(i);
    pagination.appendChild(btn);
  }

  // 다음 버튼
  if (currentPage < totalPages) {
    const nextBtn = document.createElement("button");
    nextBtn.innerText = "다음 ▶";
    nextBtn.onclick = () => renderPage(currentPage + 1);
    pagination.appendChild(nextBtn);
  }

  document.getElementById("resultSection").appendChild(pagination);
}