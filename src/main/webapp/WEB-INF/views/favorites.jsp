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
      .heart {
        font-size: 2rem;
        cursor: pointer;
        color: gray;
      }
      .heart.liked {
        color: red; /* 색칠된 하트 */
      }
    </style>
    <script>
      // XMLHttpRequest를 이용한 GET 요청 (페이지 로드 시)
      window.onload = function() {
        const heartIcon = document.getElementById("heartIcon");
        const xhr = new XMLHttpRequest();
        xhr.open("GET", "/api/favorites/cocktails/"+<%=cocktailId%>, true);
        xhr.onreadystatechange = function() {
          if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
              const flag = parseInt(xhr.responseText);
              console.log(flag);
              if (flag === 2) {
                heartIcon.classList.add("liked"); // 색칠된 하트
                heartIcon.innerHTML="&#9829";
              } else {
                heartIcon.classList.remove("liked"); // 비색칠 하트
                heartIcon.innerHTML="&#9825";
              }
            } else {
              console.error("Error fetching heart status");
            }
          }
        };
        xhr.send();
        //하트 클릭 시 POST 또는 DELETE 요청
        // heartIcon.addEventListener("click", () => {
        //   const xhr = new XMLHttpRequest();
        //   if (heartIcon.classList.contains("liked")) {
        //     // DELETE 요청 (하트를 취소하는 경우)
        //     xhr.open("DELETE", `/api/cocktails/remove`, true);
        //     xhr.setRequestHeader("Content-Type", "application/json");
        //     xhr.onreadystatechange = function() {
        //       if (xhr.readyState === XMLHttpRequest.DONE) {
        //         if (xhr.status === 200) {
        //           const response = JSON.parse(xhr.responseText);
        //           if (response.success) {
        //             heartIcon.classList.remove("liked"); // 하트 비활성화
        //           }
        //         } else {
        //           console.error("Error removing heart");
        //         }
        //       }
        //     };
        //     xhr.send(JSON.stringify({ userId, cocktailId }));
        //   } else {
        //     // POST 요청 (하트를 누른 경우)
        //     xhr.open("POST", `/api/cocktails/add`, true);
        //     xhr.setRequestHeader("Content-Type", "application/json");
        //     xhr.onreadystatechange = function() {
        //       if (xhr.readyState === XMLHttpRequest.DONE) {
        //         if (xhr.status === 200) {
        //           const response = JSON.parse(xhr.responseText);
        //           if (response.success) {
        //             heartIcon.classList.add("liked"); // 하트 활성화
        //           }
        //         } else {
        //           console.error("Error adding heart");
        //         }
        //       }
        //     };
        //     xhr.send(JSON.stringify({ userId, cocktailId }));
        //   }
        // });
      };
    </script>
</head>
<body>
<div>
    <span id="heartIcon" class="heart liked">&#9829;</span>
</div>

</body>
</html>
