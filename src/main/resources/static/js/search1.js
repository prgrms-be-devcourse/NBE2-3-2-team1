document.addEventListener("DOMContentLoaded", function () {
  const cocktailList = document.getElementById("cocktail-list");

  // 페이지 로드 시 조회수 기준으로 Top 5 가져오기
  fetchCocktailData("/api/cocktails/top/hits");

  // 조회순 버튼 클릭 이벤트
  document.getElementById("sortByHits").addEventListener("click", function () {
    fetchCocktailData("/api/cocktails/top/hits");
  });

  // 좋아요순 버튼 클릭 이벤트
  document.getElementById("sortByLikes").addEventListener("click", function () {
    fetchCocktailData("/api/cocktails/top/likes");
  });

  // 데이터를 가져와서 HTML 업데이트
  function fetchCocktailData(url) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);

    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
        if (xhr.status === 200) {
          const topCocktails = JSON.parse(xhr.responseText);
          updateCocktailList(topCocktails);
        } else {
          alert("칵테일 목록을 불러오는데 실패하였습니다. 관리자에게 문의하세요.");
          console.error("Failed to fetch data: " + xhr.status);
        }
      }
    };

    xhr.send();
  }

  // HTML 업데이트 함수
  function updateCocktailList(cocktails) {
    cocktailList.innerHTML = ""; // 기존 목록 초기화
    cocktails.forEach((cocktail) => {
      const div = document.createElement("div");
      div.className = "text";
      div.textContent = `#${cocktail.name}`;
      cocktailList.appendChild(div);
    });
  }
});

function checkLogin() {
  const xhr = new XMLHttpRequest();
  xhr.open("POST", "/login_complete", true);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        const response = JSON.parse(xhr.responseText);
        if (response.flag === 0) {
          console.log("마이 페이지로 이동");
          window.location.href = "/mypage";
        } else {
          alert(response.message);
          window.location.href = "/login";
        }
      } else {
        alert("로그인 상태 확인 실패: " + xhr.statusText);
        window.location.href = "/login";
      }
    }
  };
  xhr.send();
}

function checkLogin2() {
  const xhr = new XMLHttpRequest();
  xhr.open("POST", "/login_complete", true);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        const response = JSON.parse(xhr.responseText);
        if (response.flag === 0) {
          alert("로그인되어 있습니다. 마이 페이지로 이동하세요");
        } else {
          alert(response.message);
          window.location.href = "/login";
        }
      } else {
        alert("로그인 상태 확인 실패: " + xhr.statusText);
        window.location.href = "/login";
      }
    }
  };
  xhr.send();
}
