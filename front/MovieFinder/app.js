/**!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 로컬 데이터 부분!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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

function searchMovies() {
  const query = document.getElementById("searchInput").value.trim();
  const resultSection = document.getElementById("resultSection");

  if (!query) {
    alert("검색어를 입력하세요!");
    return;
  }

  const filteredMovies = dummyData.filter(movie =>
      movie.title.includes(query) || movie.overview.includes(query)
  );

  resultSection.innerHTML = `<h2>"${query}"에 대한 영화 목록</h2><p>총 ${filteredMovies.length}개의 영화가 검색되었습니다.</p>`;

  const cardContainer = document.createElement("div");
  cardContainer.className = "card-container";

  filteredMovies.forEach(movie => {
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
}
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/


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
