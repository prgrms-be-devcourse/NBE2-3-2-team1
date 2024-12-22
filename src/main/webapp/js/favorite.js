// JavaScript for handling reviews

document.addEventListener("DOMContentLoaded", function () {
  const reviewText = document.getElementById("reviewText");
  const submitReview = document.getElementById("submitReview");
  const reviewList = document.getElementById("reviewList");

  // Submit review
  submitReview.addEventListener("click", () => {
    const reviewContent = reviewText.value.trim();

    if (!reviewContent) {
      alert("Please enter a review.");
      return;
    }

    const xhr = new XMLHttpRequest();
    xhr.open("POST", `/api/reviews/cocktails/${cocktailId}`, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200) {
          const response = parseInt(xhr.responseText);
          console.log(response)
          if(response===2){
            reviewList.innerHTML = '';
            loadReviews();
          } else if(response === 0) {
            alert('로그인이 필요합니다.');
          } else{
            alert('댓글 등록에 실패했습니다. 자세한 내용은 관리자에게 문의하세요.');
          }

        } else {
          console.error("Error submitting review");
        }
      }
    };

    const reviewData = {
      content: reviewContent,
    };

    xhr.send(JSON.stringify(reviewData));
  });

  // Add review to the list (updated to include delete button)
  function addReviewToList(review) {
    const reviewDiv = document.createElement("div");
    reviewDiv.className = "review";
    reviewDiv.setAttribute("data-review-id", review.id);

    reviewDiv.innerHTML = `
      <div class="review-header">
          ${review.userName} - 수정 :${review.updatedAt ? calculateTimeAgo(new Date(review.updatedAt)) : '날짜정보없음'}
          <button class="delete-review" data-review-id="${review.id}">Delete</button>
      </div>
      <div class="review-content">
          ${review.content}
      </div>
    `;

    reviewList.prepend(reviewDiv);

    // Add delete button event listener
    const deleteButton = reviewDiv.querySelector(".delete-review");
    deleteButton.addEventListener("click", () => deleteReview(review.id));
  }

  // Function to delete a review
  function deleteReview(reviewId) {
    if (!confirm("Are you sure you want to delete this review?")) return;

    const xhr = new XMLHttpRequest();
    xhr.open("DELETE", `/api/reviews/cocktails/${reviewId}`, true);
    xhr.onreadystatechange = function () {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 204) {
          // 여기서 지우는것 말고 만약 삭제 반환값이 정상으로 response로 돌아오면 loadReview()호출해서 댓글뜨게 설정해주기
          const reviewDiv = reviewList.querySelector(`[data-review-id="${reviewId}"]`);
          if (reviewDiv) {
            reviewDiv.remove();
            // console.log("data-review-id: " + reviewDiv.dataset.reviewId);
          }
        } else if(xhr.status === 401){
          alert('로그인이 필요합니다.');
        } else{
          alert('댓글 삭제에 실패했습니다. 자세한 내용은 관리자에게 문의하세요.');
        }
      }
    };
    xhr.send();
  }

  // Load existing reviews
  function loadReviews() {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `/api/reviews/cocktails/${cocktailId}`, true);
    xhr.onreadystatechange = function () {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200) {
          const reviews = JSON.parse(xhr.responseText);
          reviews.forEach((review) => addReviewToList(review));
        }
        else if(xhr.status === 204){
          console.log("No review exists for cocktail id '" +cocktailId+"'");
        }
        else {
          console.error("Error loading reviews");
        }
      }
    };
    xhr.send();
  }

  function calculateTimeAgo(date) {
    const now = new Date();
    const diffInMs = now - date; // 시간 차이 (밀리초 단위)
    const diffInMinutes = Math.floor(diffInMs / 60000); // 분 단위로 변환

    if (diffInMinutes < 1) return '방금 전';
    if (diffInMinutes < 60) return `${diffInMinutes}분 전`;

    const diffInHours = Math.floor(diffInMinutes / 60);
    if (diffInHours < 24) return `${diffInHours}시간 전`;

    const diffInDays = Math.floor(diffInHours / 24);
    if (diffInDays < 7) return `${diffInDays}일 전`;

    const diffInWeeks = Math.floor(diffInDays / 7);
    if (diffInWeeks < 4) return `${diffInWeeks}주 전`;

    const diffInMonths = Math.floor(diffInDays / 30);
    if (diffInMonths < 12) return `${diffInMonths}개월 전`;

    const diffInYears = Math.floor(diffInMonths / 12);
    return `${diffInYears}년 전`;
  }


  // Initialize reviews
  loadReviews();
});


function handleRequest(icon, url, additionalClass, activeSymbol, inactiveSymbol) {
  const xhr = new XMLHttpRequest();
  xhr.open("GET", url, true);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE) {
      if (xhr.status === 200) {
        const response = parseInt(xhr.responseText);
        if (response === 2) {
          console.log(icon.id, ' : ', response);
          icon.classList.add(additionalClass);
          icon.innerHTML = activeSymbol;
        } else {
          console.log(icon.id, ' : ', response);
          icon.classList.remove(additionalClass);
          icon.innerHTML = inactiveSymbol;
        }
      } else {
        console.error(`Error processing request to ${url}`);
      }
    }
  };
  xhr.send();
}

function addIconMouseClickEventListener(icon, url, additionalClass, activeSymbol, inactiveSymbol) {
  icon.addEventListener("click", () => {
    const xhr = new XMLHttpRequest();
    if (icon.classList.contains(additionalClass)) {
      // DELETE 요청
      xhr.open("DELETE", url, true);
      xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          if (xhr.status === 200) {
            const response = parseInt(xhr.responseText);
            if (response === 2) {
              console.log(icon.id, ' : ', response);
              icon.innerHTML = inactiveSymbol;
              icon.classList.remove(additionalClass); // 하트 비활성화
            } else {
              console.log(response);
            }
          } else {
            console.error("Error removing heart");
          }
        }
      };
      xhr.send();
    } else {
      // POST 요청 (하트를 누른 경우)
      xhr.open("POST", url, true);
      xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          if (xhr.status === 200) {
            const response = parseInt(xhr.responseText);
            if (response === 2) {
              console.log(icon.id, ' : ', response);
              icon.innerHTML = activeSymbol;
              icon.classList.add(additionalClass); // 하트 활성화
            } else {
              console.log(response);
            }
          } else {
            console.error("Error adding heart");
          }
        }
      };
      xhr.send();
    }
  });
}

document.addEventListener("DOMContentLoaded", function () {
  // 아이콘 요소 가져오기
  const favoriteIcon = document.getElementById("favoriteIcon");
  const likeIcon = document.getElementById("likeIcon");

  // favoriteIcon 초기 상태 설정
  handleRequest(
      favoriteIcon,
      "/api/favorites/cocktails/" + cocktailId,
      "favorited",
      "&#9829;",
      "&#9829;"
  );

  // likeIcon 초기 상태 설정
  handleRequest(
      likeIcon,
      "/api/likes/cocktails/" + cocktailId,
      "liked",
      "&#9829;",
      "&#9829;"
  );

  // 클릭 이벤트 추가
  addIconMouseClickEventListener(
      favoriteIcon,
      "/api/favorites/cocktails/" + cocktailId,
      "favorited",
      "&#9829;",
      "&#9829;"
  );

  addIconMouseClickEventListener(
      likeIcon,
      "/api/likes/cocktails/" + cocktailId,
      "liked",
      "&#9829;",
      "&#9829;"
  );
});