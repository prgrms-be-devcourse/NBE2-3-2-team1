
// search3.js
document.addEventListener("DOMContentLoaded", function () {
  // HTML에서 data-cocktail-id 값을 가져오기
  const container = document.querySelector(".container[data-cocktail-id]");
  const cocktailId = container.dataset.cocktailId; // data-cocktail-id 값 가져오기
  console.log("cocktailId:", cocktailId);

  // 아이콘 요소 가져오기
  const favoriteIcon = document.getElementById("favoriteIcon");
  const likeIcon = document.getElementById("likeIcon");

  // favoriteIcon 초기 상태 설정
  handleRequest(
      favoriteIcon,
      "/api/favorites/cocktails/" + cocktailId,
      "favorited",
      "&#9733;", // 활성화된 별
      "&#9734;"  // 비활성화된 별
  );

  // likeIcon 초기 상태 설정
  handleRequest(
      likeIcon,
      "/api/likes/cocktails/" + cocktailId,
      "liked",
      "&#9825;", // 활성화된 하트
      "&#9825;"  // 비활성화된 하트
  );

  // 클릭 이벤트 추가
  addIconMouseClickEventListener(
      favoriteIcon,
      "/api/favorites/cocktails/" + cocktailId,
      "favorited",
      "&#9733;", // 활성화된 별
      "&#9734;"  // 비활성화된 별
  );

  addIconMouseClickEventListener(
      likeIcon,
      "/api/likes/cocktails/" + cocktailId,
      "liked",
      "&#9825;", // 활성화된 하트
      "&#9825;"  // 비활성화된 하트
  );
});

function handleRequest(icon, url, additionalClass, activeSymbol, inactiveSymbol) {
  const xhr = new XMLHttpRequest();
  xhr.open("GET", url, true);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE) {
      if (xhr.status === 200) {
        const response = parseInt(xhr.responseText);
        if (response === 2) {
          console.log(icon.id, " : ", response);
          icon.classList.add(additionalClass);
          icon.innerHTML = activeSymbol;
        } else {
          console.log(icon.id, " : ", response);
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
          if (xhr.status === 204) {
            icon.innerHTML = inactiveSymbol;
            icon.classList.remove(additionalClass); // 비활성화
            location.reload(); //새로고침
          } else if(xhr.status === 401){
            alert('로그인이 필요합니다.');
          }
          else{
            // 상태코드 500 반환시
            if(additionalClass=== "favorited"){
              alert('즐겨찾기 취소에 실패했습니다. 자세한 내용은 관리자에게 문의하세요.');
            }else{
              alert('좋아요 취소에 실패했습니다. 자세한 내용은 관리자에게 문의하세요.');
            }

          }
        }
      };
      xhr.send();
    } else {
      // POST 요청
      xhr.open("POST", url, true);
      xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          if (xhr.status === 200) {
            const response = parseInt(xhr.responseText);
            if (response === 2) {
              console.log(icon.id, " : ", response);
              icon.innerHTML = activeSymbol;
              icon.classList.add(additionalClass); // 활성화
              location.reload(); //새로고침
            } else {
              console.log(response);
            }
          } else {
            console.error("Error adding favorite/like");
          }
        }
      };
      xhr.send();
    }
  });
}

document.addEventListener("DOMContentLoaded", function () {
  const commentInput = document.getElementById("commentInput"); // 댓글 입력 필드
  const commentButton = document.getElementById("commentButton"); // 댓글 제출 버튼
  const commentList = document.getElementById("commentList"); // 댓글 리스트
  const container = document.querySelector(".container[data-cocktail-id]");
  const cocktailId = container.dataset.cocktailId; // data-cocktail-id 값 가져오기

  // 댓글 제출
  commentButton.addEventListener("click", () => {
    const commentContent = commentInput.value.trim();

    if (!commentContent) {
      alert("댓글을 입력해주세요.");
      return;
    }

    const xhr = new XMLHttpRequest();
    xhr.open("POST", `/api/reviews/cocktails/${cocktailId}`, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 204) {
          const response = parseInt(xhr.responseText);
          console.log(response)
          commentList.innerHTML = '';
          loadComments();
          } else if(xhr.status === 401){
            alert('로그인이 필요합니다.');
          } else{
            // 상태코드 500 반환시
            alert('댓글 등록에 실패했습니다. 자세한 내용은 관리자에게 문의하세요.');
          }
      }
    };

    const commentData = {
      content: commentContent,
    };

    xhr.send(JSON.stringify(commentData));
  });

  // 댓글 리스트에 추가
  function addCommentToList(comment) {
    commentInput.value = '';      //댓글 상자 초기화
    const commentDiv = document.createElement("li");
    commentDiv.className = "comment-item";
    commentDiv.setAttribute("data-comment-id", comment.id);

    commentDiv.innerHTML = `
      <div class="comment-header">
        <strong>${comment.userName}</strong> - ${comment.updatedAt ? calculateTimeAgo(new Date(comment.updatedAt)) : '날짜정보없음'}
        <button class="delete-comment" data-comment-id="${comment.id}">삭제</button>
      </div>
      <div class="comment-content">${comment.content}</div>
    `;

    commentList.prepend(commentDiv);

    // 댓글 삭제 버튼 이벤트 추가
    const deleteButton = commentDiv.querySelector(".delete-comment");
    deleteButton.addEventListener("click", () => deleteComment(comment.id));
  }

  // 댓글 삭제
  function deleteComment(commentId) {
    if (!confirm("이 댓글을 삭제하시겠습니까?")) return;

    const xhr = new XMLHttpRequest();
    xhr.open("DELETE", `/api/reviews/cocktails/${commentId}`, true);
    xhr.onreadystatechange = function () {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 204) {
          // 여기서 지우는것 말고 만약 삭제 반환값이 정상으로 response로 돌아오면 loadReview()호출해서 댓글뜨게 설정해주기
          const commentDiv = commentList.querySelector(`[data-comment-id="${commentId}"]`);
          if (commentDiv) {
            commentDiv.remove();
            // console.log("data-review-id: " + reviewDiv.dataset.reviewId);
          }
        } else if(xhr.status === 401){
          alert('로그인이 필요합니다.');
        } else{
          // 상태코드 500 반환시
          alert('댓글 삭제에 실패했습니다. 자세한 내용은 관리자에게 문의하세요.');
        }
      }
    };
    xhr.send();
  }

  // 댓글 불러오기
  function loadComments() {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `/api/reviews/cocktails/${cocktailId}`, true);
    xhr.onreadystatechange = function () {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200) {
          const comments = JSON.parse(xhr.responseText);
          commentList.innerHTML = ""; // 기존 댓글 삭제
          comments.forEach((comment) => addCommentToList(comment));
        } else if (xhr.status === 204) {
          console.log("댓글이 없습니다.");
        } else {
          console.error("Error loading comments.");
        }
      }
    };
    xhr.send();
  }

  // 시간 계산 함수
  function calculateTimeAgo(date) {
    const now = new Date();
    const diffInMs = now - date;
    const diffInMinutes = Math.floor(diffInMs / 60000);

    if (diffInMinutes < 1) return "방금 전";
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

  // 초기화
  loadComments();
});

