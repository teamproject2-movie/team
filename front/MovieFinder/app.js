function searchMovies() {
  const query = document.getElementById("searchInput").value.trim();
  const resultSection = document.getElementById("resultSection");

  if (!query) {
    alert("검색어를 입력하세요!");
    return;
  }

  const mockMovies = [
    {
      title: "어벤져스: 엔드게임",
      overview: "모든 것을 건 최후의 결전이 시작된다!",
      posterPath: "https://image.tmdb.org/t/p/w500/q6725aR8Zs4IwGMXzZT8aC8lh41.jpg"
    },
    {
      title: "기생충",
      overview: "봉준호 감독의 사회 풍자 걸작",
      posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg"
    },
    {
      title: "인터스텔라",
      overview: "우주를 넘는 사랑과 모험 이야기",
      posterPath: "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg"
    }
  ];

  const filteredMovies = mockMovies.filter(movie =>
    movie.title.includes(query) || movie.overview.includes(query)
  );

  resultSection.innerHTML = `<h2>"${query}"에 대한 영화 목록</h2><p>총 ${filteredMovies.length}개의 영화가 검색되었습니다.</p>`;

  const cardContainer = document.createElement("div");
  cardContainer.className = "card-container";

  filteredMovies.forEach(movie => {
    const card = document.createElement("div");
    card.className = "movie-card";
    card.innerHTML = `
      <img src="${movie.posterPath}" alt="포스터" />
      <h3>${movie.title}</h3>
      <p>${movie.overview}</p>
    `;
    cardContainer.appendChild(card);
  });

  resultSection.appendChild(cardContainer);

  /**  서버 연결 아직
  fetch(`http://localhost:8080/api/movies/Search?query=${encodeURIComponent(query)}`)
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
        card.innerHTML = `
          <img src="placeholder.png" alt="포스터" />
          <h3>${movie.title}</h3>
          <p>${movie.overview || "설명 없음"}</p>
        `;
        cardContainer.appendChild(card);
      });

      resultSection.appendChild(cardContainer);
    })
    .catch(error => {
      console.error("검색 에러:", error);
      alert("검색 결과가 없습니다.");
    });
    */
}
