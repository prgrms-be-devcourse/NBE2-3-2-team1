<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.programmers.cocktail.entity.Cocktails" %>
<%
    Cocktails cocktailById = (Cocktails)request.getAttribute("cocktailById");
    Long cocktailId = cocktailById.getId();
    String cocktailName = cocktailById.getName();
    String ingredients = cocktailById.getIngredients();
    String recipes = cocktailById.getRecipes();
    String category = cocktailById.getCategory();
    String alcoholic = cocktailById.getAlcoholic();
    String Image_url = cocktailById.getImage_url();
    Long hits = cocktailById.getHits();
    Long likes = cocktailById.getLikes();
    System.out.println("cocktailId: " + cocktailId);
    System.out.println("cocktailName: " + cocktailName);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Heart Status</title>
    <style>
      .favorite {
        font-size: 2rem;
        cursor: pointer;
        color: gray;
      }
      .favorite.favorited {
        color: red; /* 색칠된 하트 */
      }
      .like{
        font-size: 2rem;
        cursor: pointer;
        color: gray;
      }
      .like.liked{
        color: green;
      }
    </style>
    <script>

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
            xhr.onreadystatechange = function() {
              if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                  const response = parseInt(xhr.responseText);
                  if (response === 2) {
                    console.log(icon.id, ' : ', response);
                    icon.innerHTML=inactiveSymbol;
                    icon.classList.remove(additionalClass); // 하트 비활성화
                  }else{
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
            xhr.onreadystatechange = function() {
              if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                  const response = parseInt(xhr.responseText);
                  if (response === 2) {
                    console.log(icon.id, ' : ', response);
                    icon.innerHTML=activeSymbol;
                    icon.classList.add(additionalClass); // 하트 활성화
                  }else{
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

      window.onload = function () {
        // 아이콘 요소 가져오기
        const favoriteIcon = document.getElementById("favoriteIcon");
        const likeIcon = document.getElementById("likeIcon");

        // favoriteIcon 초기 상태 설정
        handleRequest(
            favoriteIcon,
            "/api/favorites/cocktails/"+<%=cocktailId%>,
            "favorited",
            "&#9829;",
            "&#9825;"
        );

        // likeIcon 초기 상태 설정
        handleRequest(
            likeIcon,
            "/api/likes/cocktails/"+<%=cocktailId%>,
            "liked",
            "&#9829;",
            "&#9825;"
        );

        // 클릭 이벤트 추가
        addIconMouseClickEventListener(
            favoriteIcon,
            "/api/favorites/cocktails/"+<%=cocktailId%>,
            "favorited",
            "&#9829;",
            "&#9825;"
        );

        addIconMouseClickEventListener(
            likeIcon,
            "/api/likes/cocktails/"+<%=cocktailId%>,
            "liked",
            "&#9829;",
            "&#9825;"
        );
      };

    </script>
</head>
<body>
<div>
    <span id="favoriteIcon" class="favorite">&#9825;</span>
</div>
<div>
    <span id="likeIcon" class="like">&#9825;</span>
</div>


</body>
</html>
