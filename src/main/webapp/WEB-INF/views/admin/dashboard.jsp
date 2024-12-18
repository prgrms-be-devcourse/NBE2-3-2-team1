<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DashStack - Dashboard</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="dashboard">
        <!-- 사이드바 -->
	<aside class="sidebar">
            <div class="sidebar-logo">
                <img src="images/logo.png" alt="DashStack 로고">
                <span>DashStack</span>
            </div>
            <nav class="sidebar-nav">
                <div class="sidebar-menu active">
                    <i class="fas fa-tachometer-alt"></i>
                    <span>대시보드</span>
                </div>
                <a class="sidebar-menu" href="calendar.html">
                    <i class="fas fa-calendar-alt"></i>
                    <span>캘린더</span>
                </a>
                <div class="sidebar-menu">
                    <i class="fas fa-comments"></i>
                    <span>메신저</span>
                </div>
                <div class="sidebar-menu">
                    <i class="fas fa-file-invoice-dollar"></i>
                    <span>송장</span>
                </div>
                <div class="sidebar-menu">
                    <i class="fas fa-cog"></i>
                    <span>설정</span>
                </div>
                <div class="sidebar-menu">
                    <i class="fas fa-sign-out-alt"></i>
                    <span>로그아웃</span>
                </div>
            </nav>
        </aside>


        <!-- 상단바 -->
        <header class="topbar">
            <div class="search">
                <i class="fas fa-search"></i>
                <input type="search" placeholder="검색">
            </div>
            <div class="user-info">
                <div>
                    <div>Moni Roy</div>
                    <div style="font-size: 12px; color: #666;">관리자</div>
                </div>
                <img src="images/profile.jpg" alt="프로필 이미지">
            </div>
        </header>

        <!-- 메인 콘텐츠 영역 -->
        <main class="main-content">
            <!-- 그래프 구역 (상단에 배치) -->
            <div class="chart-section" style="background: #fff; margin-bottom: 40px; border-radius:8px; padding:20px; box-sizing:border-box; height:300px;">
                <!-- 추후 그래프 라이브러리 적용 예정 -->
                <p style="color:#aaa; text-align:center; line-height:300px;">(그래프 영역)</p>
            </div>

            <!-- 대시보드 섹션(카드) -->
            <div class="stats-cards">
                <div class="card total-user">
                    <div class="card-title">Total User</div>
                    <div class="card-value">40,689</div>
                    <div class="card-info">8.5% Up from yesterday</div>
                </div>
                <div class="card total-query">
                    <div class="card-title">Total Query</div>
                    <div class="card-value">10,293</div>
                    <div class="card-info">1.3% Up from past week</div>
                </div>
                <div class="card total-comments">
                    <div class="card-title">Total Comments</div>
                    <div class="card-value">2,040</div>
                    <div class="card-info">1.8% Up from yesterday</div>
                </div>
            </div>

            <!-- 상세 랭킹 테이블 -->
            <div class="detail-rankings">
                <div class="ranking-header">
                    <div class="col product-name">Product Name</div>
                    <div class="col location">Location</div>
                    <div class="col date-time">Date - Time</div>
                    <div class="col piece">Piece</div>
                    <div class="col amount">Amount</div>
                    <div class="col status">Status</div>
                </div>
                <div class="ranking-item">
                    <div class="item-product">Bloody Merry</div>
                    <div class="item-location">6096 Marjolaine Landing</div>
                    <div class="item-date">12.09.2019 - 12.53 PM</div>
                    <div class="item-piece">423</div>
                    <div class="item-amount">$34,295</div>
                    <div class="item-status delivered">Delivered</div>
                </div>
                <div class="ranking-item">
                    <div class="item-product">Bloody Merry</div>
                    <div class="item-location">6096 Marjolaine Landing</div>
                    <div class="item-date">12.09.2019 - 12.53 PM</div>
                    <div class="item-piece">423</div>
                    <div class="item-amount">$34,295</div>
                    <div class="item-status pending">Pending</div>
                </div>
                <div class="ranking-item">
                    <div class="item-product">Bloody Merry</div>
                    <div class="item-location">6096 Marjolaine Landing</div>
                    <div class="item-date">12.09.2019 - 12.53 PM</div>
                    <div class="item-piece">423</div>
                    <div class="item-amount">$34,295</div>
                    <div class="item-status rejected">Rejected</div>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
