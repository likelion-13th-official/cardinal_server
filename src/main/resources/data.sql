TRUNCATE TABLE event_detail_images RESTART IDENTITY CASCADE;
TRUNCATE TABLE menus RESTART IDENTITY CASCADE;
TRUNCATE TABLE goods_detail_images RESTART IDENTITY CASCADE;
TRUNCATE TABLE scrap RESTART IDENTITY CASCADE;
TRUNCATE TABLE performance RESTART IDENTITY CASCADE;
TRUNCATE TABLE goods RESTART IDENTITY CASCADE;
TRUNCATE TABLE event RESTART IDENTITY CASCADE;
TRUNCATE TABLE booth RESTART IDENTITY CASCADE;
TRUNCATE TABLE map RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;
TRUNCATE TABLE amenity RESTART IDENTITY CASCADE;
-- 새로 생성된 운영 요일 테이블도 초기화
TRUNCATE TABLE booth_operating_days RESTART IDENTITY CASCADE;
TRUNCATE TABLE event_operating_days RESTART IDENTITY CASCADE;
ALTER TABLE performance ALTER COLUMN thumbnail_url TYPE TEXT;
ALTER TABLE booth
    DROP CONSTRAINT IF EXISTS booth_day_of_week_check;
ALTER TABLE event
    DROP CONSTRAINT IF EXISTS event_day_of_week_check;
ALTER TABLE event ALTER COLUMN application_form_url DROP NOT NULL;
-- 이미지 필드 타입 TEXT
ALTER TABLE booth ALTER COLUMN thumbnail_url TYPE TEXT;
ALTER TABLE booth ALTER COLUMN instagram_url TYPE TEXT;
ALTER TABLE booth ALTER COLUMN table_layout_url TYPE TEXT;
ALTER TABLE goods ALTER COLUMN description TYPE TEXT;
ALTER TABLE booth ALTER COLUMN description TYPE TEXT;

-- 1. 위치정보 MAP
INSERT INTO map (POSITION, latitude, longitude)
VALUES ('청년광장',37.5514,126.9393), --레드불 , DEFAULT
       ('청년광장',	37.5513,	126.9391), --몬스터에너지
       ('청년광장',37.5516,126.9391),
       ('J311',37.5503,126.9430), --종이잡지클럽
       ('로욜라도서관 이주연 갤러리',37.5516,126.9417),
       ('J-CY 사잇길',37.5510,126.9428),
       ('대운동장',37.5507,126.9413),
       ('대운동장',	37.5506,	126.9415), --쿠팡이츠
       ('체육관',37.5500,126.9391),
       ('R관 앞',37.5501,126.9411),
       ('K관 옆',37.5502,126.9407),
       ('엠마오관 뚜껑',37.5512,126.9410),
       ('K-GN사이',37.5503,126.9400),
       ('J관 앞',37.5505,126.9429),
       ('J관',37.5503,126.9430),
       ('소화기 전용 위치1',37.5508,126.9405),
       ('소화기 전용 위치2',37.5507,126.9406),
       ('소화기 전용 위치3',37.5511,126.9417),
       ('소화기 전용 위치4',37.5510,126.9418),
       ('소화기 전용 위치5',37.5503,	126.9408),
       ('배리어 프리 전용 위치',37.5508,126.9416),
       ('의료 본부 전용 위치',37.5504,126.9415);



-- 2. 부스
---- 2-1. 주점
-- CTE에서 반환된 데이터를 사용하여 'booth_detail_images'에 메뉴 이미지 정보를 삽입합니다.

INSERT INTO booth_operating_days (booth_id, operating_days)
SELECT
    id,
    'THU'
FROM
    booth
WHERE
    category= 'PUB';
-- CTE를 사용하여 'booth' 테이블에 데이터를 삽입하고 생성된 ID와 이름을 반환받습니다.
WITH
-- 1) 부스별 본문(description)만 따로 정의
descriptions (name, description) AS (
    VALUES
        ('[총학생회] 나루, 배', $$🧭2025 CARDINAL 총학생회 주점: 대항해시대🧭
🗺️
때는 2025년, 숨겨진 보물을 찾기 위해 나루호(號)에 모여든 선원들
배 위에서 놀고 먹으며 보물을 찾으러 가자!
▪️매장 이용 선원분들께는 마법의 소라고동(기본안주)과 웰컴드링크가 제공됩니다!
🌊EVENT
▪️나는 어떤 유형의 선장일까? 선장 유형 테스트(매장 이용 선원 only)
▪️바다가 우리를 부르고 있네. 선원 능력 고사(매장 이용 선원 only)
▪️이 남자를 본 적이 있나요? 현상금 사냥 미니게임 3종$$),
        ('[자과대] 자대 산악회', $$안녕하세요! 서강대학교 자연과학대학 제22대 학생회 [에코 Echo] 입니다.
자연과학대학 주점 ‘자대 산악회’를 소개합니다! 🏔️

🍽️ 식량창고 🍽️
해물 짬뽕 수제비, 우삼겹 짬뽕 수제비, 콘마요 불닭볶음면, 우삼겹 숙주볶음, 달콤 토마토, 얼음컵

‼️ 산행 전 안내말씀 ‼️
1️⃣ 인스타 스토리 이벤트
📸 자대 주점 사진 + @sogang_ns 태그 후 업로드 시
→ 숙취해소제 무료 증정 (선착순 30명 한정)
2️⃣ 단대비 납부자 혜택
✔️ 달콤 토마토 1개 무료 제공 (테이블당 1회)
✔️ 소주, 맥주, 막걸리 500원 할인 (테이블당 주류 1병)

‼️ 단대비 납부 확인은 ‘서강대학교 자연과학대학 학생회’ 카카오톡 채널 추가 후, 이름과 학번을 보내주시면 확인 가능합니다!$$),
        ('[경영대] 9축 밤주점', $$⚾️서강이들을 위한 야구 컨셉 주점⚾️
야구에 진심인 경영이들이 모여 만든 9축 밤주점!
야구장에서 볼 법한 직관 메뉴들과 함께 응원 열기를 느껴보세요!

- 큐브 스테이크
- 순살치킨
- 떡볶이
- 김치전
- 나초
- 파인애플
- 황도
- 아이스크림

생맥주와 함께 즐길 수 있는 다양한 안주가 준비되어 있습니다!
🔥9축 밤주점에서 야구와 함께하는 뜨거운 밤을 보내세요!🔥$$),
        ('[편입학생회] 고기에서 만나', $$안녕하세요, 편입생 주점 <고기에서 만나> 입니다! 🍖
고깃집 컨셉으로 꾸며진 저희 주점은 편입생들이 직접 구워주는 고기와 함께, 다양한 안주와 술을 즐길 수 있는 공간입니다.

✔️ 국내산 통삼겹살
✔️ 수제 김치전
✔️ 투움바 파스타
✔️ 오뎅탕
✔️ 파인애플 샤베트
✔️ 제로 아이스티

고기 한 점에 술 한 잔, 잊을 수 없는 추억을 만들어보세요!$$),
        ('[사과대] 사과씨네: SGV', $$🍎사과씨네: SGV🍎
안녕하세요, 사회과학대학 학생회입니다!
SGV에서 영화같은 하루를 보내세요!

- 투움바 파스타
- 닭강정
- 김치전
- 오다리 튀김
- 콘치즈
- 팥빙수
- 파인애플

다양한 메뉴와 함께 즐거운 시간을 보내실 수 있도록 준비했습니다!
🎬여러분의 많은 관심과 방문 부탁드립니다!🎬$$),
        ('[지융미] 맛사이드 아웃', $$안녕하세요! 커뮤니케이션학부 학생회입니다.
맛사이드 아웃 주점에 오신 것을 환영합니다!

- 닭꼬치
- 김치전
- 어묵탕
- 감자튀김
- 쫄깃 버블 아이스크림

다양한 감정의 맛을 표현한 안주들과 함께 즐거운 시간을 보내세요!
✨여러분을 기다리고 있겠습니다!✨$$),
        ('[인공지능] 바 에이아이', $$안녕하세요, 서강대학교 학우 여러분! 가을 축제 CARDINAL에서 인공 X AI 자전이 야심 차게 주점을 운영합니다. 맛과 분위기, 모두 잡은 저희 부스 '프롬프트 한 잔'이 여러분을 기다리고 있습니다!
🍸 논알콜 칵테일
	블루 밀키스
	깔루아 밀크
	모히또
술을 마시지 않아도 축제 분위기를 마음껏 즐길 수 있어요!
🎁 이벤트
	안주 두 개 이상 주문 시, 테이블당 칵테일 한 잔을 무료로 드립니다!
	전날과 당일에 진행하는 부스에서 칵테일 무료 쿠폰을 받아가세요!
여러분의 많은 방문 부탁드립니다! 인공 X AI 자전이 준비한 특별한 주점에서 축제의 밤을 함께 즐겨요!$$),
        ('[컴공] MVP 푸드코트', $$골든 골 스낵 파크에서 땀 흘린 당신, 진정한 스포츠 MVP입니다!
경기의 승리를 기념하며, MVP만을 위한 특별한 야식 부스로 초대합니다~
	💪 근수저들을 위한 든든한 고기 세트부터
	🔥 매콤하고 시원한 안주
	✨ 달콤한 디저트까지!
오늘의 MVP, 당신을 위한 특식을 마음껏 즐겨보세요!$$),
        ('[경제대] 쇼미더''주량''', $$주(酒)의 랩 배틀이 시작된다!
안주도 FLEX, 술도 FLEX, 분위기도 FLEX.
많은 관심과 방문 부탁드립니다$$),
        ('[인문대] 술로지옥', $$🔥🏝 술로지옥 🏝🔥

안녕하세요 서강대학교 학우 여러분!
올해 CARDINAL 축제에서 인문대학이 준비한 주점은,
낯선 만남, 새로운 인연, 그리고 짜릿한 설렘이 가득한,
🔥🏝<술로지옥>🏝🔥 입니다!

💚메뉴💚
✔️천국도😇 특화 메뉴
- 두부김치
- 파인애플샤베트
- 황도
✔️지옥도👿 특화 메뉴
- 불닭까르보나라
- 매콤닭꼬치
- 매콤오뎅나베
✔️공통 메뉴
- 콘치즈
- 츄러스
- 얼음컵
- 공기밥

💚이벤트💚
✔️테이블 매칭
- 솔로 탈출 기원..🥹 테이블 매칭으로 새로운 인연을 만나보세요!
✔️인스타그램 이벤트
- 인문대 주점 방문 인증샷을 인스타그램에 올려주시면, 추첨을 통해 다양한 상품을 드립니다!
✔️게임 이벤트
- 다양한 게임을 통해 술자리 분위기를 한층 더 뜨겁게!

이번 축제, <술로지옥>에서 잊지 못할 추억을 만들어보세요!
여러분의 많은 관심과 방문 부탁드립니다!$$),
        ('[공과대] 너로 정했다! 가랏, 공돌이!', $$⚡️너로 정했다! 가랏, 공돌이!⚡️
안녕하세요, 공학부 학생회입니다.
공학부 주점에 오신 것을 환영합니다!

- 닭강정
- 김치전
- 오뎅탕
- 감자튀김
- 팥빙수

다양한 메뉴와 함께 즐거운 시간을 보내세요!
🔥여러분을 기다리고 있겠습니다!🔥$$),
        ('[H.U.G] H.U.G 주점', $$안녕하세요, 국제인문학부 학생회입니다.
H.U.G 주점에 오신 것을 환영합니다!

- 닭강정
- 김치전
- 오뎅탕
- 감자튀김
- 팥빙수

다양한 메뉴와 함께 즐거운 시간을 보내세요!
🔥여러분을 기다리고 있겠습니다!🔥$$),
        ('[EXPANDED] La Cantina Expandida', $$안녕하세요, Expandida 주점입니다.
La Cantina Expandida에 오신 것을 환영합니다!

- 타코
- 나초
- 퀘사디아
- 감자튀김
- 츄러스

멕시코의 맛과 열정을 느껴보세요!
🔥여러분을 기다리고 있겠습니다!$$)
),
-- 2) 나머지 필드(썸네일/공지/링크 등)
base_data (name, thumbnail_url, notice, instagram_url, table_layout_url) AS
    (VALUES
         ('[총학생회] 나루, 배', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%EC%B4%9D_optimized.jpg', '항해 컨셉으로 나룻배를 형상화한 주점', 'https://www.instagram.com/sogang_naru/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[자과대] 자대 산악회', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%EC%9E%90_optimized.jpg', '산악회 컨셉', 'https://www.instagram.com/sogang_ns/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[경영대] 9축 밤주점', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%EA%B2%BD_optimized.jpg', '야구장', 'https://www.instagram.com/sgbusiness_official/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[편입학생회] 고기에서 만나', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%ED%8E%B8_optimized.jpg', '고깃집', 'https://www.instagram.com/sogang_transfer/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[사과대] 사과씨네: SGV', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%EC%82%AC_optimized.jpg', '영화관 CGV', 'https://www.instagram.com/apple_sum/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[지융미] 맛사이드 아웃', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%EB%A7%9B_optimized.jpg', '인사이드 아웃', 'https://www.instagram.com/sogang_cmas/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[인공지능] 바 에이아이', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%EC%8F%98%EC%9D%B8%EA%B3%B5%EC%9E%90%EC%A0%84_optimized.jpg', '서비스형 주점', 'https://www.instagram.com/sgu_ai_official/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[컴공] MVP 푸드코트', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%EC%8F%98%EC%BB%B4_optimized.jpg', '경기 MVP가 먹는 특식', 'https://www.instagram.com/sogang_sgcs_official/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[경제대] 쇼미더''주량''', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%EC%83%81_optimized.jpg', '랩, 머니', 'https://www.instagram.com/sogangecon_dfficial/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[인문대] 술로지옥', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%EB%AC%B8_optimized.jpg', '솔로지옥(천국도 vs 지옥도)', 'https://www.instagram.com/sogang_moon/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[공과대] 너로 정했다! 가랏, 공돌이!', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_%EA%B3%B5_optimized.jpg', '전화기시에 어울리는 포켓몬 4마리의 대결 구도', 'https://www.instagram.com/sgu_engineering_official/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[H.U.G] H.U.G 주점', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_hug_optimized.jpg', '한식주점', 'https://www.instagram.com/soganghug_official/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png'),
         ('[EXPANDED] La Cantina Expandida', 'https://d9are2p0j0wzf.cloudfront.net/pub/pub_expanded_optimized.jpg', '멕시칸 주점', 'https://www.instagram.com/soganghug_official/expandedkr/', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png')
    ),
-- 3)
inserted_booths AS (
    INSERT INTO booth (
                       category, booth_type, name, location_id, description,
                       thumbnail_url, view_count, start_time, end_time,
                       is_operating, notice, instagram_url, table_layout_url, notice_updated_at
        )
        SELECT
            'PUB',
            '주점',
            p.name,
            (SELECT id FROM map WHERE position = '대운동장' limit 1),
            d.description,        -- ← description + 공통 문구(원하면 제거)
            p.thumbnail_url,
            1,
            '19:00',
            '23:00',
            false,
            p.notice,
            p.instagram_url,
            p.table_layout_url,
            NOW()
        FROM base_data p
                 JOIN descriptions d USING (name)
        RETURNING id, name
)
-- 주점 이미지들
INSERT INTO booth_detail_images (booth_id, image_order, image_url)
SELECT
    ib.id,
    menu_data.image_order,
    menu_data.image_url
FROM
    inserted_booths ib
        JOIN (

        VALUES
            ('[총학생회] 나루, 배', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/%EC%B4%9D_%EB%A9%94%EB%89%B4%ED%8C%90_optimized.jpg'),
            ('[총학생회] 나루, 배', 2, 'https://d9are2p0j0wzf.cloudfront.net/menu/%EC%B4%9D_%EB%A9%94%EB%89%B4%ED%8C%902_optimized.jpg'),
            ('[총학생회] 나루, 배', 3, 'https://d9are2p0j0wzf.cloudfront.net/menu/%EC%B4%9D_%EC%9D%B4%EB%B2%A4%ED%8A%B8_optimized.jpg'),
            ('[총학생회] 나루, 배', 4, 'https://d9are2p0j0wzf.cloudfront.net/menu/%EC%B4%9D_%EC%9D%B4%EB%B2%A4%ED%8A%B82_optimized.jpg'),
            ('[총학생회] 나루, 배', 5, 'https://d9are2p0j0wzf.cloudfront.net/menu/%EC%B4%9D_%EC%9D%B4%EB%B2%A4%ED%8A%B83_optimized.jpg'),
            ('[자과대] 자대 산악회', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EC%9E%90_optimized.jpg'),
            ('[경영대] 9축 밤주점', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EA%B2%BD%EC%98%811_optimized.jpg'),
            ('[경영대] 9축 밤주점', 2, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EA%B2%BD%EC%98%812_optimized.jpg'),
            ('[편입학생회] 고기에서 만나', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%ED%8E%B8_optimized.jpg'),
            ('[사과대] 사과씨네: SGV', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EC%82%AC_optimized.jpg'),
            ('[사과대] 사과씨네: SGV',2,'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EC%82%AC%EC%9D%B4%EB%B2%A4%ED%8A%B8_optimized.jpg'),
            ('[지융미] 맛사이드 아웃', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EB%A7%9B1_optimized.jpg'),
            ('[지융미] 맛사이드 아웃', 2, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EB%A7%9B2_optimized.jpg'),
            ('[컴공] MVP 푸드코트', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EC%8F%98%EC%BB%B4_optimized.jpg'),
            ('[컴공] MVP 푸드코트',2,'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EC%8F%98%EC%BB%B4%EC%9D%B4%EB%B2%A4%ED%8A%B8_optimized.jpg'),
            ('[경제대] 쇼미더''주량''', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EA%B2%BD%EC%A0%9C_optimized.jpg'),
            ('[인문대] 술로지옥', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EB%AC%B8_optimized.jpg'),
            ('[인문대] 술로지옥', 2, 'https://d9are2p0j0wzf.cloudfront.net/pub_menu/문_주류_optimized.jpg'),
            ('[인공지능] 바 에이아이',1,'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EC%9D%B8%EA%B3%B5%EC%9E%90%EC%A0%84_optimized.jpg'),
            ('[인공지능] 바 에이아이',2,'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EC%9D%B8%EA%B3%B5%EC%9E%90%EC%A0%84%EC%9D%8C%EB%A3%8C_optimized.jpg'),
            ('[인공지능] 바 에이아이',3,'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EC%9D%B8%EA%B3%B5%EC%9E%90%EC%A0%84%EC%9D%B4%EB%B2%A4%ED%8A%B8_optimized.jpg'),
            ('[공과대] 너로 정했다! 가랏, 공돌이!', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EA%B3%B5_optimized.jpg'),
            ('[공과대] 너로 정했다! 가랏, 공돌이!', 2, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EA%B3%B5%EC%9D%B4%EB%B2%A4%ED%8A%B8_optimized.jpg'),
            ('[공과대] 너로 정했다! 가랏, 공돌이!', 3, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EA%B3%B5%EC%9D%B4%EB%B2%A4%ED%8A%B82_optimized.jpg'),
            ('[공과대] 너로 정했다! 가랏, 공돌이!', 4, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_%EA%B3%B5%EC%9D%B4%EB%B2%A4%ED%8A%B83_optimized.jpg'),
            ('[H.U.G] H.U.G 주점', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_hug_optimized.jpg'),
            ('[EXPANDED] La Cantina Expandida', 1, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_expanded1_optimized.jpg'),
            ('[EXPANDED] La Cantina Expandida', 2, 'https://d9are2p0j0wzf.cloudfront.net/menu/menu_expanded2_optimized.jpg')
    ) AS menu_data(booth_name, image_order, image_url) ON ib.name = menu_data.booth_name;
--주점 운영 여부
INSERT INTO booth_operating_days (booth_id, operating_days)
SELECT
    id,
    'THU'
FROM
    booth
WHERE
    category= 'PUB';
--주점 대표 메뉴 삽입
insert  into menus (booth_id,name,price)
select
    (SELECT id FROM booth WHERE booth.name = data.pubName),
    data.menuName,
    data.price
from (
         values
             ('[경영대] 9축 밤주점','홈런볼 아이스크림',6000),
             ('[경영대] 9축 밤주점','크림새우',12500),
             ('[경영대] 9축 밤주점','닭강정',12000),
             ('[경제대] 쇼미더''주량''','수육 (1-2인)',12900), /*(3-4인) 22,900원*/
             ('[경제대] 쇼미더''주량''','수육 (3-4인)',22900), /*(3-4인) 22,900원*/
             ('[경제대] 쇼미더''주량''','어묵탕 ',9900),
             ('[경제대] 쇼미더''주량''','두부김치 ',11900),
             ('[공과대] 너로 정했다! 가랏, 공돌이!','피카츄 돈가스(1마리)',3800), /* 2마리 6000원 */
             ('[공과대] 너로 정했다! 가랏, 공돌이!','피카츄 돈가스(2마리)',6000), /* 2마리 6000원 */
             ('[공과대] 너로 정했다! 가랏, 공돌이!','불닭 덮밥',10000),
             ('[공과대] 너로 정했다! 가랏, 공돌이!','모둠 시즈닝 감자튀김',8000),
             ('[사과대] 사과씨네: SGV','버터구이 오징어',10000),
             ('[사과대] 사과씨네: SGV','우삼겹 불닭게티',11000),
             ('[사과대] 사과씨네: SGV','만두 & 즉석떡볶이',13000),
             ('[컴공] MVP 푸드코트','삼겹살 & 목살',10000),
             ('[컴공] MVP 푸드코트','골벵이 소면',8000),
             ('[컴공] MVP 푸드코트','샤베트',4000),
             ('[인공지능] 바 에이아이','감자전',11000),
             ('[인공지능] 바 에이아이','치즈팽이버섯전',12000),
             ('[인공지능] 바 에이아이','돼지김치구이',17000),
             ('[인문대] 술로지옥','우삼겹 숙주볶음',13900),
             ('[인문대] 술로지옥','콘치즈 불닭볶음면',10900),
             ('[인문대] 술로지옥','소고기 마라탕',14900),
             ('[자과대] 자대 산악회','해물 짬뽕 수제비',13900),
             ('[자과대] 자대 산악회','콘마요 불닭볶음면',7900),
             ('[자과대] 자대 산악회','우삼겹 숙주볶음',12900),
             ('[지융미] 맛사이드 아웃','삼겹두부김치',18000),
             ('[지융미] 맛사이드 아웃','감자찌글이 + 주먹밥',20000),
             ('[지융미] 맛사이드 아웃','소세지 야채볶음',11000),
             ('[총학생회] 나루, 배','해물파전',14000),
             ('[총학생회] 나루, 배','해물칼국수',10000),
             ('[총학생회] 나루, 배','스낵 플래터',5000),
             ('[편입학생회] 고기에서 만나','삼겹살',13900),
             ('[편입학생회] 고기에서 만나','참치주먹밥',4900),
             ('[편입학생회] 고기에서 만나','파인샤베트',6900),
             ('[EXPANDED] La Cantina Expandida','나쵸플레이트',12000),
             ('[EXPANDED] La Cantina Expandida','콘옥수수 튀김',8000),
             ('[EXPANDED] La Cantina Expandida','아이스크림&츄러스',7000),
             ('[H.U.G] H.U.G 주점','K-BBQ set(삼겹살 한상차림)',10000),
             ('[H.U.G] H.U.G 주점','Bibim-myeon(비빔면)',4000),
             ('[H.U.G] H.U.G 주점','Dried pollack(먹태)',8000)

     ) as data(pubName,menuName,price);

---- 2-2. 제휴
--booth - partnership
-- DELETE FROM booth_operating_days WHERE booth_id in (select id from booth where booth_type='제휴');
-- DELETE FROM BOOTH WHERE booth_type='제휴';

WITH
    -- 1. 삽입할 부스 데이터에 장소별로 순번(rn)
    booth_data_with_rn AS (
        SELECT
            *,
            ROW_NUMBER() OVER (PARTITION BY location_name ORDER BY name) AS rn
        FROM (
                 VALUES
                     ('타로 카운셀러','체육관','타로 보세요 여러분들','https://.../part_counselor1.png', 'https://.../part_counselor1.png'),
                     ('틱톡라이브', '청년광장', '2025 글로벌 TikTok LIVE...', 'https://.../part_tiktok.jpg', 'https://.../part_tiktok.jpg'),
                     ('몬스터에너지', '청년광장', '몬스터에너지 음료를 맛볼 수 있는...', 'https://.../part_monster.png', 'https://.../KakaoTalk_...png'),
                     ('레드불', '청년광장', '레드불 음료와 함께 날개를!', 'https://.../part_redbull.png', 'https://.../KakaoTalk_...png'),
                     ('인도푸드', 'K관 옆', '향신료 가득한 인도 음식...', 'https://.../part_indomie.png', 'https://.../KakaoTalk_...png'),
                     ('쿠팡잇츠', '대운동장', '쿠팡이츠 랜덤 쿠폰 이벤트 ', 'https://.../part_indomie.png', 'https://.../KakaoTalk_...png'),
                     ('롯데호텔', '대운동장', '2025 CARDINAL with 롯데호텔', 'https://.../part_lotte.png', 'https://.../KakaoTalk_...png')
             ) AS data(name, location_name, description, thumbnail_url, logo_image_url)
    ),
    -- 2. map 테이블의 위치 데이터에도 장소별로 순번(rn)
    locations_with_rn AS (
        SELECT
            id,
            position,
            ROW_NUMBER() OVER (PARTITION BY position ORDER BY id) AS rn
        FROM map
    )
-- 3. 두 임시 테이블을 '장소 이름'과 '순번'으로 JOIN하여 INSERT 실행
INSERT INTO booth (
    booth_type, category, name, location_id, description,
    thumbnail_url, view_count, start_time, end_time, is_operating, logo_image_url
)
SELECT
    '제휴' AS booth_type,
    'PARTNERSHIP' AS category,
    b.name,
    l.id, -- 👈 짝지어진 location_id
    b.description,
    b.thumbnail_url,
    1 AS view_count,
    TIME '12:00',
    TIME '18:00',
    false AS is_operating,
    b.logo_image_url
FROM booth_data_with_rn b
         JOIN locations_with_rn l ON b.location_name = l.position AND b.rn = l.rn;

INSERT INTO booth_operating_days (booth_id, operating_days)
SELECT
    (SELECT id FROM booth WHERE name = data.booth_name),
    data.operating_day
FROM (
         VALUES
             -- 틱톡라이브 (월-금)
             ('틱톡라이브', 'MON'),
             ('틱톡라이브', 'TUE'),
             ('틱톡라이브', 'WED'),
             ('틱톡라이브', 'THU'),
             ('틱톡라이브', 'FRI'),
             -- 타로 카운셀러 (월-금)
             ('타로 카운셀러', 'MON'),
             ('타로 카운셀러', 'TUE'),
             ('타로 카운셀러', 'WED'),
             ('타로 카운셀러', 'THU'),
             ('타로 카운셀러', 'FRI'),
             -- 몬스터에너지 (월)
             ('몬스터에너지', 'MON'),
             -- 레드불 (화)
             ('레드불', 'TUE'),
             -- 인도푸드 (화-목)
             ('인도푸드', 'TUE'),
             ('인도푸드', 'WED'),
             ('인도푸드', 'THU'),
             ('인도푸드', 'THU'),
             -- 쿠팡잇츠 (금) , 롯데호텔 (수)
             ('쿠팡잇츠', 'FRI'),
             ('롯데호텔', 'WED')
     ) AS data(booth_name, operating_day);

---- 2-3. 마당사업
-------- 교내단체 마당사업 : descriptoin 추가
-- 마당사업 소개글 줄바꿈 프론트와 상의
WITH
    -- 1. 변수처럼 사용할 description 값들을 ID와 함께 정의합니다.
    descriptions (id, description) AS (
        VALUES
            (1, E'🎉 서강대학교 응원 TRIPATHY 부스 운영 안내 🎉

            안녕하세요, 서강대학교 학우 여러분!
            9월 CARDINAL을 맞이하여, 서강대학교 응원단 트라이파시(TRIPATHY)가 부스를 운영합니다✨

            💌 사랑은 타이밍!
            트라이파시 단독공연 <그대에게10> 공연날인 11월 14일을 스톱워치로 11.14초에 정확히 멈춘 분께 소정의 간식을 드립니다!

            💭 당신에게 사랑이란 무엇인가요?
            여러분의 마음속 ‘사랑의 정의’를 포스트잇에 담아주세요.
            짧은 단어 하나라도, 긴 문장이라도 괜찮습니다.
            여러분이 적어주신 순간들은 모여, 11월 14일 단독공연 <그대에게10>에서 하나의 큰 이야기가 될 예정입니다!

            📣 응원타올 판매
            트라이파시와 함께 축제를 즐길 수 있는 또 다른 방법!
            현장판매: 5,000원
            사전판매: 4,500원
            부스 운영 시간(12:00~16:30, 혹은 12:00~18:00)에 현장 수령 가능합니다.

            📱 인스타 팔로우 이벤트
            인스타그램 @tripathy_sogang 팔로우 후, 추첨을 통해 5분께 스타벅스 10,000원 상품권을 드립니다!
            트라이파시의 새로운 소식을 인스타그램에서 가장 먼저 만나보세요.
            서강대학교 학우 여러분의 많은 관심과 참여 부탁드립니다! 💖'),
            (2, E'🔮 21C Hermit 타로 텔링 부스 🔮
            안녕하세요. 서강대학교 중앙 타로 동아리 21C Hermit입니다!
            사랑, 진로, 인간관계… 마음속에 담아둔 고민들을 타로 카드로 풀어보세요.
            당신의 모든 질문을 정성껏 들어드립니다.

            📅 일시
            9월 22일(월) 12:00-18:00
            9월 23일(화) 12:00-18:00

            🚩 위치
            R관 앞

            💸 비용
            ₩4,000

            🔍 허밋이 더 궁금하다면?
            https://linktr.ee/21chermit?utm_source=linktree_profile_share&ltsid=a13bd434-d760-49a2-af8e-2b7c13f7b029
            '),
            (3, E'✨별반 부스 안내✨

            안녕하세요, 서강대학교 학우 여러분!
            CARDINAL 축제를 맞이해 💫천문 동아리 별반💫이 ''별반이랑 우주정복'' 부스를 운영합니다!
            시원한 음료와 재미있는 게임, 그리고 상품 뽑기를 통해 축제를 더욱 다채롭게 즐겨보세요😚

            🪐부스 소개🪐
            🍹시원한 음료 판매🍹
            밀키스를 넣어 만든 블루레몬에이드에 우주인 모양 얼음을 퐁당!👽🛸
            가격: 2500원

            🃏게임 대결🃏
            우주를 담은 할리갈리 카드로 운영진과 게임 대결을 펼쳐보세요~ 게임에서 이기면 음료 500원 할인 쿠폰을 드린답니다😆

            👾추가이벤트 : 상품 뽑기👾
            음료도 마시고 상품도 받을 기회!! 음료를 구매하시면 뽑기 참여 기회를 드립니다. 캡슐 뽑기 기계를 통해 상품을 뽑을 수 있으며, 간식류와 외계인 와앙포카 등이 준비되어 있습니다~!
            '),
            (4, E'❤️팀제미나이 <Gemini와 함께하는 나만의 캐릭터 만들기> 부스 안내❤️

            안녕하세요. 저희 팀제미나이가 이번에 서강대학교 카디널 축제에서 Google Al Pro for Students의 가입 혜택 및 주요 기능을 홍보하는 부스를 운영하게 되었습니다. 해당 부스에서 Gemini의 가입을 도와드리고 다양한 활용 기능을 재미있게 체험시켜 드릴 예정입니다!

            자세한 내용은 카드 뉴스와 하단 글 참고 바라며 많은 관심 부탁드립니다.

            👩🏻‍💻부스명
            제미나이(Gemini)와 함께하는 나만의 캐릭터 만들기

            1.Gemini를 활용하여 만든 하나뿐인 자신만의 캐릭터 라벨 증정 🏷️
            2. Google AI Pro 가입 안내

            Gemini의 Imagen 4 기능을 활용하여 자신만의 캐릭터, 이미지를 제작해 볼 수 있는 체험형 부스입니다. 부스 방문 시 원하는 캐릭터의 이미지 요구사항을 말씀해 주시면, 이를 바탕으로 프롬프트를 작성하여 세상에 하나뿐인 나만의 캐릭터를 만들어 드립니다. 생성된 캐릭터는 라벨로 인화하여 즉시 증정해 드릴 예정입니다.

            캐릭터 제작 가이드라인을 제공하며, 구체적인 프롬프트 작성 팁을 활용해 더욱 높은 퀄리티의 캐릭터를 만나보실 수 있습니다. 또한, 부스 참여 및 가입자를 대상으로 추첨을 통한 특별한 상품 이벤트도 진행될 예정입니다!

            [문의사항]
            인스타그램 @gemini_syndrome


            이상 있을 시 연락 부탁드립니다, 대표 연락처 : 010-8366-0790
            '),
            (5, '보건소'),
            (6, E'목적: 딥페이크·마약류이용 성범죄 예방 캠페인
                        내용: 마약 간이시약 키트 배부, 간식 제공, 퀴즈 맞추고 상품받기, 피해자 보호지원제도 안내, 전담경찰관과의 소통 등
                        참여 간식도 있으니 많은 참여 부탁드립니다!'),
            (7, '2025년 하반기 장애인식개선 캠페인 ')
    ),
    -- 2. 삽입할 데이터  ( description_id 사용)
    temp_data (name, description_id, thumbnail_url, start_time, end_time) AS (
        VALUES
            ('트파는 사랑을 싣고', 1, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_트라이파시_optimized.jpg', TIME '12:00', TIME '16:30'),
            ('21C Hermit 타로 텔링 부스', 2, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_21CHermit_optimized.jpg', TIME '12:00', TIME '18:00'),
            ('별반이랑 우주정복', 3, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_별반_optimized.jpg', TIME '12:00', TIME '18:00'),
            ('나만의 캐릭터 만들기', 4, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_제미나이_optimized.jpg', TIME '10:00', TIME '18:00'),
            ('건강생활실천 캠페인', 5, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_보건소_optimized.jpg', TIME '14:00', TIME '17:00'),
            ('성폭력·교제폭력·스토킹 ZERO 캠퍼스', 6, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_성평등_optimized.jpg', TIME '14:00', TIME '16:00'),
            ('장애인식개선 캠페인', 7, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_장애학생지원센터_optimized.jpg', TIME '11:00', TIME '16:00')
    )
-- 3. 위에서 정의한 임시 테이블들을 JOIN하여 최종 INSERT 실행
INSERT INTO booth (
    booth_type, category, name, location_id, description,
    thumbnail_url, view_count, start_time, end_time, is_operating
)
SELECT
    '마당사업' AS booth_type,
    'YARD_PROJECT' AS category,
    td.name,
    (SELECT id FROM map WHERE position = 'R관 앞') AS location_id,
    d.description AS description,
    td.thumbnail_url,
    100 AS view_count,
    td.start_time,
    td.end_time,
    false AS is_operating
FROM temp_data td
         JOIN descriptions d ON td.description_id = d.id; -- ID를 기준으로 JOIN

INSERT INTO booth_operating_days (booth_id, operating_days)
SELECT
    (SELECT id FROM booth WHERE name = data.booth_name),
    data.operating_day
FROM (
         VALUES
             ('트파는 사랑을 싣고', 'MON'),
             ('트파는 사랑을 싣고', 'TUE'),
             ('21C Hermit 타로 텔링 부스', 'MON'),
             ('21C Hermit 타로 텔링 부스', 'TUE'),
             ('별반이랑 우주정복', 'MON'),
             ('별반이랑 우주정복', 'TUE'),
             ('나만의 캐릭터 만들기', 'MON'),
             ('건강생활실천 캠페인', 'THU'),
             ('성폭력·교제폭력·스토킹 ZERO 캠퍼스', 'TUE'),
             ('장애인식개선 캠페인', 'THU')

     ) AS data(booth_name, operating_day);

-------- 단대 마당사업 : edit by yeeun
-- booth에 INSERT 하면서 booth_id 반환 :
WITH
    -- 1. description 모음
    descriptions (id, description) AS (
        VALUES
            (1, E'자연과학대학 부스 ''👻엑, Ooooh!🕸''를 소개합니다!

📆 일시 : 9월 24일(수)-9월 25일(목) 11:30-17:30
📍 장소 : 대운동장 1번 부스
🍭간식부스🍭
  🧋스모어 마시멜로우 초코라떼 - 3500원
  🍪스모어 마시멜로우 쿠키 - 3000원
  ☕아이스 아메리카노 - 1500원
  🧇아이스크림 크로플 - 3500원

🎲게임부스🎲
  🎃촉감게임
  🎃젤리 옮기기

❗게임 성공 시, 간식부스에서 500원 할인이 제공됩니다.
❗️자연과학대학 단대비 납부자에 한하여 스모어 마시멜로우 초코라떼 1잔이 무료 제공됩니다.
❗️재료 소진 시 조기마감합니다.

문의사항은 서강대학교 자연과학대학 인스타(@sogang_ns) DM으로 연락주시길 바랍니다. 감사합니다. :)
'),  -- 길면 E''로 줄바꿈 포함
            (2, E'⚾️ 홈런왕 김경영 ⚾️🏟️

경영대학 학우 여러분, 안녕하세요!
더욱 즐거운 축제를 위해 경영대에서 오직 여러분들만을 위해 준비한 낮부스를 소개해드립니다 🙌🏻

🏅장소: 대운동장 3번

📋 메뉴 📋
🍜 김치말이국수
🍙 참치주먹밥
🥤 홈런볼 + 논알콜맥주(한정 수량) / 콜라 / 사이다

*김치말이국수, 참치주먹밥 중 택 1*

⛳️ 게임 ⛳️
미니 야구 게임기
(2루타 이상 소정의 경품 증정)
>> 경품: 하리보 젤리

🍋‍🟩 대상 🍋‍🟩
경영 제1전공자 무료 제공
(학생증 필수 지참, ‼️하루에 인당 2메뉴(메인1+디저트1) 까지 가능‼️)

경영 학우분들의 많은 참여 부탁드립니다 ☺️☺️
'),
            (3, E'💡🎞️🎈사과씨네; SGV🎈🎞️💡

안녕하세요 서강대학교 학우 여러분!
9월 CARDINAL을 맞이하여 사회과학대학 학생회 [숨;SUM]에서 저녁 주점을 운영합니다🤗

가을의 CARDINAL을 맞이하여 축제 분위기가 무르익은 서강대학교 교정, 낭만과 사랑을 챙길 영화관
🎈사과씨네; SGV🎈가 열립니다!

코미디, 액션, 로맨스, 스릴러-! 다양한 장르가 여러분을 기다립니다.

🎪장소: 대운동장 6번 부스
🎪일시: 9월 25일 목요일 18시~
🎪메뉴
🎬”팡팡 웃음이 터지는 코미디 영화” 팝콘&나쵸
🎬”순식간에 몰입하는 매운맛 액션 영화” 우삽겹 불닭게티
🎬”달콤짭짤한 로맨스 영화” 버터구이오징어
🎬”매콤하게 긴장되는 스릴러 영화” 만두&즉석떡볶이
🎪이벤트: 나랑 SGV갈래, 아니면 나한테 SGV나 올래.
🎬영화 명장면을 보고 영화 제목을 맞출 경우, 1,000원 할인쿠폰이 팡팡!!

서강대학교 학우여러분의 많은 관심과 참여 부탁드립니다~!!!

*기타 문의사항은 사회과학대학 오픈채팅(https://open.kakao.com/o/sLbH7Rtf)이나 사과대 인스타그램(@apple__sum_)디엠으로 주시면 해결 도와드리겠습니다.
'),
            (4, E'🌈 영화 <인사이드 아웃> 속 감정들이 이제는 부스에서 여러분을 기다립니다!!
🙋‍♀️🙋‍♂️ 감정에 취하고, 맛에 취하고, 이벤트까지 즐겨봐요!

📍 장소: 대운동장 8번 부스
🎈 주최: 지식융합미디어대학 제6대 학생회 LinC

✨ 이건 꼭 해야지! 부스 체험 소개 ✨
1️⃣ 감정 조언 캡슐 뽑기
오늘 내 기분, 너무 복잡하다고?
감정 박스에 손을 넣어 당신에게 딱 맞는 조언 캡슐을 뽑아보세요!
랜덤으로 찾아오는 힐링 한 줄🧠💫
2️⃣ 영화 속 감정 퀴즈
그 순간 ‘라일리’는 어떤 감정을 느꼈을까?
<인사이드 아웃> 속 명장면을 보고 감정을 맞히면 간식 쏜다!
추리력도, 감정이입력도 모두 발휘될 순간🎬

🥤 감정도 목이 마르다구요~?
💙 “슬픔이 블루레몬에이드” 무료 증정!
부스에 오기만 해도, 누구나 한 잔!
지치고 더운 축제 속 시원함 +1'),
            (5, '데이터 필요'),
            (6, E'🍡 골든 골 스낵 파크 🍡
짜릿한 승리와 맛있는 간식을 동시에!

서강대학교 9월 CARDINAL 축제를 맞아 컴퓨터공학과의 특별한 이벤트.
스포츠 스타디움 미니게임 & 푸드존이 준비된 “골든 골 스낵 파크”에 여러분을 초대합니다~!

역대급 승리를 향한 짜릿한 미니게임에 도전하고, 승리의 기쁨이 배가 되는 맛있는 간식도 놓치지 마세요.
각 게임을 완료하고 인증 도장을 하나라도 모으면, 25일 저녁 주점에서 MVP로 선정되어 특별한 혜택을 드립니다!
장소: 대운동장 9번 부스

🏆 스포츠 미니게임
오타니 상대로 3K?! - 승리투수가 되어 3개의 삼진을 잡아내세요!
서강 그랑프리 - 짜릿한 스피드와 컨트롤로 트랙을 정복하세요!
스포츠 뇌지컬 챌린지! - 스포츠 지식으로 승리하는 퀴즈 한판!
🍽️ 푸드존
소떡소떡
떡볶이
아이스티
아이스크림'),
            (7, E'Show me the money⛓️💰

📆 일시 : 9월 24일(수)-9월 25일(목) 11:30-17:00
📍 장소 : 대운동장 10번 부스

게임부스 설명🎲🎮
잰말놀이게임🏃‍♂️: Diction은 생명! 빠른 속도로 발음을 틀리지 않고 읽어라!
청개구리 절대음감🐸: 기존의 절대음감은 가라! 한 음씩 내리는 절대음감📉
지폐맞추기💸: 이 지폐는 어느 나라의 지폐일까요?
가격맞추기💲🤑: 4개의 물건 중 가장 비싼 물건을 찾아라!

게임에 성공하신 학우분들께는 금목걸이🥇...가 아닌 골드바 초콜릿🍫과 주점 상품권을 드립니다!'),
            (8, E'한순간의 선택이 당신의 운명을 바꿀 🎲♠️문스베이거스♠️🎲가 열립니다!

♟️장소 : 대운동장 11번 부스

♟️간식

카나페 : 3,000원 🥨
🍫 누텔라 & 바나나 🍌
🍓딸기잼 & 치즈 🧀
🧀 치즈 & 참치마요 & 토마토 🍅
크림 소다 : 2,000원 🍦🥤
메론 소다 : 2,000원 🍈🥤


🎮 게임부스 🎮

🎯문스베이거스 샷 !
다트를 던져 풍선을 맞히면 점수 획득 ! 최대 100점의 기회를 잡으세요 !
🎰스핀 오브 문스
세 장의 카드가 동시에 멈추는 순간, 운명의 조합이 당신을 기다립니다.
두 장 이상 일치 → 50점 / 세 장 일치 → 100점!
🎲다이스 룰렛
홀 or 짝, 단 한 번의 선택. 당신의 운이 어디로 향할까 ? 맞히면 50점 GET !
🎲히든 다이스
흔들리는 주사위 소리, 감각만이 당신의 무기 ! 정확히 맞히면 70점!


🎉추가 이벤트
📸 부스 인증샷 업로드 이벤트
부스에서 사진 찍고 SNS에 인증하면 럭키드로우를 참여하실 수 있는 기회가 주어집니다 !
🎁 럭키드로우 경품
1등: 주점 메인메뉴 🍖
2등: 주점 사이드메뉴 🍗
3등: 카나페 🧀
4등: 소다 🥤
5등: 몬스터 에너지 음료⚡ or 꽝 😅


🌙 운명은 언제나 모험하는 자의 편!
올 가을, 서강대학교 축제의 🎲♠️문스베이거스♠️🎲로 초대합니다.'),
            (9, E'C&M 칼텍스 (화공·기계공학과 부스)
📍 위치: 대운동장 출입구 바로 왼편, 12번 부스
🍴 메뉴: 오레오 쉐이크, 나초 치즈컵'),
            (10, E'번쩍번쩍 반도체공장 (전자·시스템반도체공학과 부스)
📍 위치: 대운동장 출입구 바로 왼편, 12번 부스
🍴 메뉴: 웨이퍼 크로플, 220V 에너지 드링크' ||
                 E'🛍️ 굿즈: 공대/전자 스티커, 전자/시반 티셔츠
                 '),
            (11,E'🍒 하늬모아 🍒
서강대학교 학생홍보대사 하늬가람이 준비한 특별한 축제 부스!
하늬가람의 감성을 캔모아 콘셉트로 담아냈습니다 ✨
[하늬에이드] 🍹
서쪽의 하늬, 그리고 서강의 상징인 빨간색 체리 에이드
[가람요거트] 🥛
강을 뜻하는 가람, 시원한 파란빛 요거트 음료
그리고 인스타그램 이벤트에 참여하면, 캔모아처럼 달콤한 식빵 + 생크림을 드려요.
하늬가람과 함께하는 작은 즐거움, [하늬모아]에서 꼭 만나보세요 💖
')
    ),
    -- 2. booth 삽입용 데이터 (description_id 참조)
    booths_to_insert (name, description_id, thumbnail_url) AS (
        VALUES
            ('웬즈데이/할로윈', 1, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_자과대_optimized.jpg'),
            ('홈런왕 김경영', 2, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_경_optimized.jpg'),
            ('아삭토스트', 3, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_사과대_optimized.jpg'),
            ('맛사이드 아웃', 4, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_맛_optimized.jpg'),
            ('cAsIno', 5, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_쏘(컴)_optimized.jpg'),
            ('골든 골 스낵 파크', 6, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_인공자전_optimized.jpg'),
            ('가제 쇼미더’머니’', 7, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_상_optimized.jpg'),
            ('문스베이거스 Moons Vegas', 8, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_문_optimized.jpg'),
            ('C&M 칼텍스', 9, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_화공기계_optimized.jpg'),
            ('번쩍번쩍 반도체 공장', 10, 'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_전자시반_optimized.jpg'),
            ('하늬모아',11,'https://d9are2p0j0wzf.cloudfront.net/마당사업/마당사업_하늬가람_optimized.jpg')
    ),
    -- 3. booth 테이블에 insert
    inserted_booths AS (
        INSERT INTO booth (
                           booth_type, category, name, description, thumbnail_url,
                           view_count, start_time, end_time, is_operating, location_id
            )
            SELECT
                '마당사업',
                'YARD_PROJECT',
                b.name,
                d.description,
                b.thumbnail_url,
                1,
                '12:00:00',
                '18:00:00',
                false,
                m.id
            FROM booths_to_insert b
                     JOIN descriptions d ON b.description_id = d.id
                     CROSS JOIN map m
            WHERE m.position = '대운동장' limit 1
            RETURNING id
    )
-- 4. booth_operating_days 삽입
INSERT INTO booth_operating_days (booth_id, operating_days)
SELECT id, day
FROM inserted_booths, (VALUES ('WED'), ('THU')) AS v(day);





-- 2-4. 푸드트럭
-- 소빵 : 썸네일 수정 완료 : yeeun
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '소빵', 8, '닭강정', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/%EC%86%8C%EB%B9%B5_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '닭강정 소', 8000),
    ((SELECT id FROM new_booth), '닭강정 대', 15000);
-- 오늘은
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '오늘은', 8, '타꼬야끼', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/foodtruck_오늘은_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '타코야끼 7p', 5000),
    ((SELECT id FROM new_booth), '타꼬야끼 15p', 10000);

-- 스트릿피자
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '스트릿피자', 8, '화덕피자3종', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/foodtruck_스트릿피자_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '화덕피자 3종 R', 12000),
    ((SELECT id FROM new_booth), '화덕피자 3종 L', 20000);

-- 퍼플베어
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '퍼플베어', 8, '부타동', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/foodtruck_퍼플베어_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '부타동', 10000),
    ((SELECT id FROM new_booth), '차슈동', 12000);
-- 오야붕
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '오야붕', 8, '야끼소바/오꼬노미야끼', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/foodtruck_오야붕_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '야끼소바', 10000),
    ((SELECT id FROM new_booth), '불야끼소바', 10000),
    ((SELECT id FROM new_booth), '오꼬노미야끼', 12000);
-- 상식스키친
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '상식스키친', 8, '불초밥', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/foodtruck_상식스_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '소고기불초밥 8p', 10000);
-- 명품닭꼬치
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '명품닭꼬치', 8, '닭꼬치', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/foodtruck_닭꼬치_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '닭꼬치', 5500);
-- 흥하리푸드
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '흥하리푸드', 8, '크림/칠리새우', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/foodtruck_흥하리푸드_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '칠리새우 8p', 10000),
    ((SELECT id FROM new_booth), '레몬크림새우 8p', 10000),
    ((SELECT id FROM new_booth), '다주새우 8p', 15000);
-- 탑초이스
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '탑초이스', 8, '스테이크/덮밥', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/foodtruck_팁초이스_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '스테이크 싱글', 13000),
    ((SELECT id FROM new_booth), '스테이크 더블', 22000),
    ((SELECT id FROM new_booth), '스테이크 덮밥', 13000),
    ((SELECT id FROM new_booth), '스테이크 세트', 20000);
-- 속초시장명물닭강정
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '속초시장명물닭강정', 8, '염통꼬치', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/foodtruck_속초명물닭강정_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '염통꼬치 11p', 10000),
    ((SELECT id FROM new_booth), '염통꼬치 23p', 20000),
    ((SELECT id FROM new_booth), '염통꼬치 35p', 30000);
-- 윤쉐프
WITH new_booth AS (
    INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
        VALUES ('푸드트럭', 'FOOD_TRUCK', '윤쉐프', 8, '츄러스/아이스크림', 'https://d9are2p0j0wzf.cloudfront.net/foodtruck/foodtruck_윤쉐프_optimized.jpg', 500, '12:00:00', '22:00:00', TRUE)
        RETURNING id
)
INSERT INTO menus (booth_id, name, price)
VALUES
    ((SELECT id FROM new_booth), '츄러스', 4000),
    ((SELECT id FROM new_booth), '아이스크림 츄러스', 6000),
    ((SELECT id FROM new_booth), '회오리 감자', 5000);

select * from booth where booth_type='포토부스';

---- 2-5. 포토부스 by yeeun /*thumbnail 더미. */
INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, start_time, end_time, is_operating)
SELECT
    '포토부스',
    'PHOTO_BOOTH',
    data.name,
    (SELECT id FROM map WHERE position = data.location_name limit 1), -- 위치 이름으로 id를 동적으로 조회
    E'프레임 소개\n' ||
    E'1. 동아리사랑해 프레임 : 사랑하는 동아리원들과 자신이 속해있는 동아리 이름을 찾아보자\n' ||
    E'2. 캔젤네컷 프레임 : 키는 너네가 알아서 맞춰라.\n' ||
    E'3. 2025 CARDINAL 프레임 : 25카디널 기본 프레임. 기본은 필수로 찍어줘야 함이 인지상정.\n',
    data.thumbnail,
    1,
    '00:00:00',
    '23:59:00',
    true

FROM (
         VALUES /*thumbnail 더미. */
             ('포토부스 1','https://d9are2p0j0wzf.cloudfront.net/%EA%B0%80%EB%82%98%EB%94%94/KakaoTalk_20250703_164910895.png','청년광장'),
             ('포토부스 2','https://d9are2p0j0wzf.cloudfront.net/%EA%B0%80%EB%82%98%EB%94%94/KakaoTalk_20250703_164910895.png','체육관'),
             ('포토부스 3','https://d9are2p0j0wzf.cloudfront.net/%EA%B0%80%EB%82%98%EB%94%94/KakaoTalk_20250703_164910895.png','K-GN사이'),
             ('포토부스 4','https://d9are2p0j0wzf.cloudfront.net/%EA%B0%80%EB%82%98%EB%94%94/KakaoTalk_20250703_164910895.png','J관')
     ) AS data(name,thumbnail,location_name);

--동아리 사랑해 프레임
INSERT INTO booth_detail_images (image_order, booth_id, image_url)
SELECT
    1, -- 'pub' 타입인 부스의 ID
    id,
    'https://d9are2p0j0wzf.cloudfront.net/포토부스/photobooth_동아리사랑해_optimized.jpg
'
FROM
    booth
WHERE
    category= 'PHOTO_BOOTH';
--캔젤네컷
INSERT INTO booth_detail_images (image_order, booth_id, image_url)
SELECT
    2,
    id,
    'https://d9are2p0j0wzf.cloudfront.net/포토부스/photobooth_캔젤네컷_optimized.jpg'
FROM
    booth
WHERE
    category= 'PHOTO_BOOTH';

INSERT INTO booth_operating_days (booth_id, operating_days)
SELECT
    id, -- 'pub' 타입인 부스의 ID
    'ALWAYS'-- 모든 행에 동일하게 삽입할 값
FROM
    booth
WHERE
    category= 'PHOTO_BOOTH';

-- 3. 이벤트
INSERT INTO event (
    name, location_id, description, thumbnail_url,
    view_count, start_time, end_time, application_form_url,
    is_operating
)
SELECT
    data.name,
    -- CASE 문을 사용해 이벤트 이름에 따라 map 테이블에서 동적으로 location_id를 조회
    CASE
        WHEN data.name IN ('Campus Clash', '캠프어스(Camp Us)', '멍!🐶', 'ID카드 제작', '녹기 전에 X 2025 CARDINAL')
            THEN (SELECT id FROM map WHERE position = '청년광장' limit 1)
        WHEN data.name = 'CONNECT SOGANG : 종이잡지클럽'
            THEN (SELECT id FROM map WHERE position = 'J311')
        WHEN data.name = '동아리상영회'
            THEN (SELECT id FROM map WHERE position = '로욜라도서관 이주연 갤러리')
        WHEN data.name = '동아리 야외展'
            THEN (SELECT id FROM map WHERE position = 'J-CY 사잇길')
        WHEN data.name IN ('야외영화상영', 'SHRINKID')
            THEN (SELECT id FROM map WHERE position = '대운동장' limit 1)
        WHEN data.name IN ( '2025 서강대학교 CARDINAL KED 골든벨 with 한국경제신문','clash of knowledge')
            THEN (SELECT id FROM map WHERE position = '체육관')
        END,
    data.description,
    data.thumbnail_url,
    1, -- view_count 값을 1
    data.start_time,
    data.end_time,
    data.application_form_url,
    FALSE -- is_operating 값을 일괄적으로 FALSE로 설정합니다.
FROM (
         VALUES
             ('Campus Clash', '누구나 쉽고 간단하게 참여할 수 있는 미니게임 부스', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/event/event_CampusClash.png', TIME '12:00', TIME '18:00',NULL),
             ('CONNECT SOGANG : 종이잡지클럽', '만남 · 교감 · 성장 — 종이잡지클럽과 함께 담아내는 우리의 이야기', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png', TIME '19:00', NULL,NULL),
             ('동아리상영회', '서강만화창작부 작품, 추천 애니메이션 상영 / 서강영화공동체 작품 및 상영', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png', TIME '10:00', TIME '18:00',NULL),
             ('동아리 야외展', '사진 동아리 서광회, 시각 예술 동아리 EXPANDED 작품 전시', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png', TIME '12:00', TIME '17:00',NULL),
             ('야외영화상영', '선선한 가을 바람 맞으며 즐기는 야외 영화 상영', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png', TIME '19:00', NULL,NULL),
             ('캠프어스(Camp Us)', '캠퍼스에서 모두 함께 즐기는 우리만의 캠핑', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/event/event_CampUs.jpg', TIME '18:00', TIME '22:00',NULL),
             ('멍!🐶', '아무 것도 하지 않는 시간의 가치를 경험하는 힐링 프로그램', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/event/event_멍.png', TIME '16:00', TIME '18:00',NULL),
             ('ID카드 제작', 'College KID로 돌아가기 위한 첫 걸음', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png', TIME '12:00', TIME '18:00',NULL),
             ('녹기 전에 X 2025 CARDINAL', '서강인을 사로잡은 염리동의 보석 <녹기 전에> 팝업 부스', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png', TIME '14:00', NULL,NULL),
             ('SHRINKID', '대운동장에서 열리는 SHRINKID 공연', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/event/event_shrinkid.jpg', TIME '12:00', TIME '18:00',NULL),
             ('2025 서강대학교 CARDINAL KED 골든벨 with 한국경제신문', '한국경제신문과 함께하는 골든벨 퀴즈 이벤트', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png', TIME '14:00', TIME '17:00',NULL),
             ('clash of knowledge', '서강에서 울리는 골든벨, 한국경제신문과 함께합니다!', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png', TIME '13:00', TIME '17:00',NULL)
     ) AS data(name, description, thumbnail_url, start_time, end_time, application_form_url);

INSERT INTO event_operating_days (event_id, operating_days)
SELECT
    (SELECT id FROM event WHERE name = data.event_name), -- 이벤트 이름으로 id를 동적으로 조회
    data.operating_day
FROM (
         VALUES
             ('Campus Clash', 'MON'),
             ('Campus Clash', 'TUE'),
             ('CONNECT SOGANG : 종이잡지클럽', 'TUE'),
             ('동아리상영회', 'THU'),
             ('동아리 야외展', 'MON'),
             ('야외영화상영', 'WED'),
             ('캠프어스(Camp Us)', 'TUE'),
             ('멍!🐶', 'MON'),
             ('ID카드 제작', 'MON'),
             ('ID카드 제작', 'TUE'),
             ('ID카드 제작', 'WED'),
             ('녹기 전에 X 2025 CARDINAL', 'WED'),
             ('SHRINKID', 'WED'),
             ('SHRINKID', 'THU'),
             ('2025 서강대학교 CARDINAL KED 골든벨 with 한국경제신문', 'WED'),
             ('clash of knowledge', 'WED')
     ) AS data(event_name, operating_day);


-- 4. 공연

---- 4-1. 화 버스킹
WITH inserted_performance AS (
    INSERT INTO performance (category, name, description, thumbnail_url, view_count, location_id, start_time, end_time, is_operating)
        select
            'CLUB',
            data.name,
            data.description,
            data.thumbnail_url,
            1,
            (select id from map where position='청년광장' limit 1),
            '18:30:00',
            '20:30:00',
            FALSE
        FROM (
                 VALUES
                     -- 화요일 버스킹
                     ('애벌레',  '밀레 43기 신입기수가 선보이는 가을 감성 노래', 'https://d9are2p0j0wzf.cloudfront.net/etc/버스킹1_optimized.jpg'),
                     ('joe park',  '팝/알앤비 솔로 보컬의 커버 및 자작곡 무대', 'https://d9are2p0j0wzf.cloudfront.net/etc/버스킹2_optimized.jpg'),
                     ('조니워커 블루',  '기계공학과 밴드 블루노트 보컬 듀오의 감성 어쿠스틱 공연', 'https://d9are2p0j0wzf.cloudfront.net/etc/버스킹3_optimized.jpg'),
                     ('해피빈',  '팝과 알앤비를 장르를 중심으로 활동하고 있는 솔로 보컬', 'https://d9are2p0j0wzf.cloudfront.net/etc/버스킹4_optimized.JPG'),
                     ('경빈과 현준',  '보컬과 통기타 듀오가 선사하는 가을 버스킹', 'https://d9are2p0j0wzf.cloudfront.net/etc/버스킹5_optimized.JPG'),
                     ('무대공포증', '무대공포증 극복을 위한 용기 있는 솔로 노래', 'https://d9are2p0j0wzf.cloudfront.net/etc/버스킹6_optimized.JPG')
             ) as data(name,description,thumbnail_url)

        RETURNING id
)
INSERT INTO performance_operating_days (performance_id, operating_days)
SELECT id, 'TUE'
FROM inserted_performance;

---- 4-2. 목금 동아리공연
INSERT INTO performance (
    category, name, description, thumbnail_url,
    view_count, location_id, start_time, end_time, is_operating
)
SELECT
    'CLUB',                                             -- 공통: 카테고리
    data.name,                                          -- 고유: 공연 팀 이름
    data.description,                                   -- 고유: 설명
    data.thumbnail_url,                                 -- 고유: Thumbnail URL
    1,                                                  -- 공통: 조회수
    (SELECT id FROM map WHERE position = '대운동장' limit 1),     -- 공통: 위치 ID (동적 조회)
    data.start_time,                                    -- 고유: 시작 시간
    data.end_time,                                      -- 고유: 종료 시간
    FALSE                                               -- 공통: 운영 여부
FROM (
         VALUES
             -- 목요일 동아리 공연
             ('S.H.O.C.K', '서강대 유일 중앙 스트릿댄스 동아리의 창작 안무 공연', 'https://d9are2p0j0wzf.cloudfront.net/동아리로고/SHOCK_optimized.jpg', TIME '19:00', TIME '21:30'),
             ('Messy-G', '힙합, 알앤비, 팝을 아우르는 다채로운 장르의 공연', 'https://d9are2p0j0wzf.cloudfront.net/동아리로고/%EB%A9%94%EC%8B%9C%EC%A7%80_optimized.jpg', TIME '19:00', TIME '21:30'),
             ('ABYSS', '유일무이 중앙 흑인음악 동아리 ABYSS의  R&B, 힙합 DIY 공연', 'https://d9are2p0j0wzf.cloudfront.net/동아리로고/ABYSS_optimized.jpg', TIME '19:00', TIME '21:30'),
             ('온빛', 'K-pop 댄스 커버 전문 동아리 온빛의 퍼포먼스', 'https://d9are2p0j0wzf.cloudfront.net/동아리로고/ONBEAT_optimized.jpg', TIME '19:00', TIME '21:30'),

             -- 금요일 동아리 공연
             ('광야', '중앙 하드락 밴드의 하드락과 대중가요 무대', 'https://d9are2p0j0wzf.cloudfront.net/동아리로고/광야_optimized.jpg', TIME '15:30', TIME '18:30'),
             ('노래문화연구회 맥박', '서강대 유일한 노래문화연구회 맥박이 선보이는 밴드 음악', 'https://d9are2p0j0wzf.cloudfront.net/동아리로고/MACBAK_optimized.jpg', TIME '15:30', TIME '18:30'),
             ('서강만화창작부', '만화창작부 밴드의 J-POP, 애니메이션 OST 공연', 'https://d9are2p0j0wzf.cloudfront.net/동아리로고/%EC%84%9C%EB%A7%8C%EC%B0%BD_optimized.jpg', TIME '15:30', TIME '18:30'),
             ('에밀레', '서강대 유일 창작곡 밴드의 모두가 즐기는 신나는 무대', 'https://d9are2p0j0wzf.cloudfront.net/동아리로고/EMILLES_optimized.jpg', TIME '15:30', TIME '18:30'),
             ('킨젝스', '중앙 락밴드 킨젝스의 축제를 위한 스페셜 락 공연', 'https://d9are2p0j0wzf.cloudfront.net/동아리로고/킨젝스_optimized.jpg', TIME '15:30', TIME '18:30'),
             ('트라이파시', '트라이파시의 힘찬 응원 무대', 'https://d9are2p0j0wzf.cloudfront.net/동아리로고/club_%ED%8A%B8%EB%9D%BC%EC%9D%B4%ED%8C%8C%EC%8B%9C1_optimized.jpg', TIME '15:30', TIME '18:30')
     ) AS data(name, description, thumbnail_url, start_time, end_time);
---- 4-3. 금 아티스트
INSERT INTO performance (
    category, name, description, thumbnail_url,
    view_count, location_id, start_time, end_time, is_operating
)
SELECT
    'ARTIST',
    data.name,
    data.description,
    data.thumbnail_url,
    1,
    (SELECT id FROM map WHERE position = '대운동장' limit 1),     -- 공통: 위치 ID (동적 조회)
    TIME '18:30',
    TIME '21:00',
    FALSE
FROM (
         VALUES
             ('체리필터', '아티스트 체리필터 공연', 'https://d9are2p0j0wzf.cloudfront.net/artist/%EC%95%84%ED%8B%B0%EC%8A%A4%ED%8A%B8_%EC%B2%B4%EB%A6%AC%ED%95%84%ED%84%B0_optimized.jpg'),
             ('프로미스나인', '아티스트 프로미스나인 공연', 'https://d9are2p0j0wzf.cloudfront.net/artist/%EC%95%84%ED%8B%B0%EC%8A%A4%ED%8A%B8_%ED%94%84%EB%AF%B8%EB%82%98_optimized.jpg'),
             ('위너', '아티스트 위너 공연', 'https://d9are2p0j0wzf.cloudfront.net/artist/%EC%95%84%ED%8B%B0%EC%8A%A4%ED%8A%B8_%EC%9C%84%EB%84%88_optimized.jpg'),
             ('빈지노', '아티스트 빈지노 공연', 'https://d9are2p0j0wzf.cloudfront.net/artist/%EC%95%84%ED%8B%B0%EC%8A%A4%ED%8A%B8_%EB%B9%88%EC%A7%80%EB%85%B8_optimized.jpg')
     ) AS data(name, description, thumbnail_url);

INSERT INTO performance_operating_days (performance_id, operating_days)
SELECT
    (SELECT id FROM performance WHERE name = data.performance_name), -- 공연 이름으로 id
    data.operating_day
FROM (
         VALUES
             --CLUB,ARTIST
             -- 목요일 공연
             ('S.H.O.C.K', 'THU'),
             ('Messy-G', 'THU'),
             ('ABYSS', 'THU'),
             ('온빛', 'THU'),

             -- 금요일 공연
             ('광야', 'FRI'),
             ('노래문화연구회 맥박', 'FRI'),
             ('서강만화창작부', 'FRI'),
             ('에밀레', 'FRI'),
             ('킨젝스', 'FRI'),
             ('트라이파시', 'FRI'),
             ('체리필터', 'FRI'),
             ('프로미스나인', 'FRI'),
             ('위너', 'FRI'),
             ('빈지노', 'FRI')


     ) AS data(performance_name, operating_day);

----- 4-4. 영화제
INSERT INTO performance (category, name, description, thumbnail_url, view_count, location_id, start_time, end_time, is_operating)
VALUES
    ('FILM', 'MOVIE NIGHT', '선선한 가을 밤, 감성 충만한 야외 영화관에서 힐링하세요.', 'https://cardinal2025.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20250703_164910895.png', 1, (select id from map where position='대운동장' limit 1), '19:00:00', '21:00:00', FALSE);

INSERT INTO performance_operating_days (performance_id, operating_days)
SELECT
    id,
    'WED'
FROM
    performance
WHERE
    category='FILM';


-- 9/18 12:29 by yeeun
-- 5. 부대시설
INSERT INTO amenity (name, location_id)
SELECT
    data.amenity_name,
    (SELECT id FROM map WHERE position = data.location_name) -- 위치 이름으로 id를 동적으로 조회
FROM (
         VALUES
             ('분리수거', 'J관 앞'),
             ('분리수거', 'K-GN사이'),
             ('분리수거', '엠마오관 뚜껑'),
             --('대피로', '강의실'),
             ('소화기', '소화기 전용 위치1'),
             ('소화기', '소화기 전용 위치2'),
             ('소화기', '소화기 전용 위치3'),
             ('소화기', '소화기 전용 위치4'),
             ('소화기', '소화기 전용 위치5'),
             ('배리어 프리', '배리어 프리 전용 위치'),
             ('의료 본부', '의료 본부 전용 위치')
     ) AS data(amenity_name, location_name);

-- 6. 굿즈
-- : 이미지 추가해야합니다. 금요일만 대운동장으로 변경하기.
-- 6. 굿즈
-- : 이미지 추가 및 금요일 판매 장소 변경 적용
INSERT INTO goods (name, price, description, thumbnail_url, view_count, location_id)
SELECT
    v.name,
    v.price,
    v.description,
    v.thumbnail_url,
    1,
    m.id
FROM (VALUES
          ('FOOTBALL(black)', 35000, E'size 1, 2, 3\n키치한 무드를 담아낸 축구 레플리카.\n\n             라인을 활용하여 활기찬 실루엣을 완성하고, 두 종류의 색상을 반전시켜 위트 있는 대비와 개성을 더했다.\n\n             롱슬리브와 숏슬리브 두 가지 버전으로 제작되어 계절과 상황에 맞게 선택할 수 있으며, 언제 어디서든 자신만의 스타일을 즐기는 대학생들의 에너지를 담아냈다.\n\n             특전 증정 이벤트\n\n             - 5만원 이상 구매 시 말랑키링 증정\n\n             - 7만원 이상 구매 시 말랑키링&핀버튼 증정\n', 'https://d9are2p0j0wzf.cloudfront.net/MD/thumb/md_thumb_football_black_optimized.jpg'),
          ('FOOTBALL(red)', 38000, E'size 1, 2, 3\n키치한 무드를 담아낸 축구 레플리카.\n\n             라인을 활용하여 활기찬 실루엣을 완성하고, 두 종류의 색상을 반전시켜 위트 있는 대비와 개성을 더했다.\n\n             롱슬리브와 숏슬리브 두 가지 버전으로 제작되어 계절과 상황에 맞게 선택할 수 있으며, 언제 어디서든 자신만의 스타일을 즐기는 대학생들의 에너지를 담아냈다.\n\n             특전 증정 이벤트\n\n             - 5만원 이상 구매 시 말랑키링 증정\n\n             - 7만원 이상 구매 시 말랑키링&핀버튼 증정\n', 'https://d9are2p0j0wzf.cloudfront.net/MD/thumb/md_thumb_football_red_optimized.jpg'),
          ('COLLEGE KID T-shirt', 22000, E'color 2 (black/grey)\n size 1,2\n\n             언제 어디서든 당당한 모습의 서강 콜리지 키드들의 무드를 담아낸 레터링.\n\n             세미크롭 기장감으로 트렌디하면서도 활동적인 실루엣을 완성하고, 메인 로고는 아플리케 자수를 통해 입체적인 디테일을 살려냈다.\n\n             깔끔한 디자인 속에서도 대학생 특유의 자유롭고 당당한 에너지를 담아, 데일리룩과 캠퍼스룩 모두에 자연스럽게 어울릴 수 있도록 제작되었다.\n\n             특전 증정 이벤트\n\n             - 5만원 이상 구매 시 말랑키링 증정\n\n             - 7만원 이상 구매 시 말랑키링&핀버튼 증정\n', 'https://d9are2p0j0wzf.cloudfront.net/MD/thumb/md_thumb_college_kid_t_optimized.jpg'),
          ('CARDINAL LOGO T-shirt', 25000, E'color 2 (black/red)\n size F\n\n             2025 CARDINAL의 메인 오브제를 활용한 오버핏 티셔츠.\n\n             로고는 아플리케 자수를 활용하여 입체감을 강조하고 빈티지한 무드를 더했다.\n\n             루즈한 실루엣으로 제작되어 트렌디하면서도 편안한 착용감을 제공하며, 무채색 베이스 컬러 위에 로고 포인트를 배치해 미니멀하면서도 강렬한 인상을 준다.\n        특전 증정 이벤트\n\n             특전 증정 이벤트\n\n             - 5만원 이상 구매 시 말랑키링 증정\n\n             - 7만원 이상 구매 시 말랑키링&핀버튼 증정\n', 'https://d9are2p0j0wzf.cloudfront.net/MD/thumb/md_thumb_cardinal_logo_t_optimized.jpg'),
          ('i CAP', 25000, E'※주의※ 이 모자는 아이(i)만 쓸 수 있음. 어른은 쓸 수 없음.\n\n             언제 어디서든 기죽지 말고 아이처럼 행동해도 되는 콜리지 키드를 위한 모자.\n\n             트렌디한 실루엣으로 제작되어 데일리하게 사용할 수 있게 제작되었으며, 뒤로 써도 귀여움을 형성한다.\n             특전 증정 이벤트\n\n             - 5만원 이상 구매 시 말랑키링 증정\n\n             - 7만원 이상 구매 시 말랑키링&핀버튼 증정\n', 'https://d9are2p0j0wzf.cloudfront.net/MD/thumb/md_thumb_cap_optimized.jpg'),
          ('TIE', 15000, E'서강고등학교 학생들 모여라!\n\n             어디서도 찾아볼 수 없는 25카디널만의 색이 가득 담긴 MD이다.\n\n             메인 오브제와 와인레드 타이로 벨트, 넥타이 등 스타일링에 다양하게 활용해보자.\n\n             특전 증정 이벤트\n\n             - 5만원 이상 구매 시 말랑키링 증정\n\n             - 7만원 이상 구매 시 말랑키링&핀버튼 증정\n', 'https://d9are2p0j0wzf.cloudfront.net/MD/thumb/md_thumb_tie_optimized.jpg'),
          ('CANGEL KEY RING(10cm)', 15000, E'2025 CARDINAL 공식 마스코트 캔젤이 인형 키링으로 나타났다.\n\n             이 키링을 가진 자는 25카디널을 제대로 즐겼다는 흔적을 남기게 되는 것.\n\n             10년 후, 20년 후에도 캔젤이와 함께하며 25카디널을 기억하길 바란다.\n             특전 증정 이벤트\n\n             - 5만원 이상 구매 시 말랑키링 증정\n\n             - 7만원 이상 구매 시 말랑키링&핀버튼 증정\n', 'https://d9are2p0j0wzf.cloudfront.net/MD/thumb/md_thumb_keyring_optimized.jpg'),
          ('STRING BAG', 12000, E'2025 CARDINAL 메인 오브제 스트링백\n특전 증정 이벤트\n\n             - 5만원 이상 구매 시 말랑키링 증정\n\n             - 7만원 이상 구매 시 말랑키링&핀버튼 증정\n', 'https://d9are2p0j0wzf.cloudfront.net/MD/thumb/md_thumb_stringbag_optimized.jpg'),
          ('TATOO STICKER', 4000, E'9/24(수)부터 판매\n 특전 증정 이벤트\n\n             - 5만원 이상 구매 시 말랑키링 증정\n\n             - 7만원 이상 구매 시 말랑키링&핀버튼 증정\n', 'https://d9are2p0j0wzf.cloudfront.net/MD/thumb/md_thumb_tatoosticker_optimized.jpg'),
          ('PIN BUTTON', 3000, E'콜리지 키드의 덕목이 적힌 핀버튼을 찾아 나만의 필수템을 적어보자.\n 특전 증정 이벤트\n\n             - 5만원 이상 구매 시 말랑키링 증정\n\n             - 7만원 이상 구매 시 말랑키링&핀버튼 증정\n', 'https://d9are2p0j0wzf.cloudfront.net/MD/thumb/md_thumb_pinbutton_optimized.jpg')
     ) AS v(name,price,description,thumbnail_url)
         CROSS JOIN map m
WHERE m.position = '청년광장' limit 1;

---

-- goods_detail_images 테이블에 상세 이미지 추가
-- Images for CARDINAL LOGO T-shirt
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 1, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_CARDINAL_LOGO_T1_optimized.jpg'
FROM goods WHERE name = 'CARDINAL LOGO T-shirt';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 2, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_CARDINAL_LOGO_T2_optimized.jpg'
FROM goods WHERE name = 'CARDINAL LOGO T-shirt';

-- Images for COLLEGE KID T-shirt
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 1, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_COLLEGEKID_T1_optimized.jpg'
FROM goods WHERE name = 'COLLEGE KID T-shirt';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 2, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_COLLEGEKID_T2_optimized.jpg'
FROM goods WHERE name = 'COLLEGE KID T-shirt';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 3, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_COLLEGEKID_T3_optimized.jpg'
FROM goods WHERE name = 'COLLEGE KID T-shirt';


-- Images for STRING BAG
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 1, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_STRINGBAG_1_optimized.JPG'
FROM goods WHERE name = 'STRING BAG';

-- Images for FOOTBALL(black)
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 1, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_FOOTBALL_UNIFORM1_optimized.jpg'
FROM goods WHERE name = 'FOOTBALL(black)';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 2, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_FOOTBALL_UNIFORM2_optimized.jpg'
FROM goods WHERE name = 'FOOTBALL(black)';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 3, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_FOOTBALL_UNIFORM3_optimized.jpg'
FROM goods WHERE name = 'FOOTBALL(black)';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 4, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_FOOTBALL_UNIFORM4_optimized.jpg'
FROM goods WHERE name = 'FOOTBALL(black)';

-- Images for FOOTBALL(red)
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 1, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_FOOTBALL_UNIFORM1_optimized.jpg'
FROM goods WHERE name = 'FOOTBALL(red)';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 2, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_FOOTBALL_UNIFORM2_optimized.jpg'
FROM goods WHERE name = 'FOOTBALL(red)';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 3, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_FOOTBALL_UNIFORM3_optimized.jpg'
FROM goods WHERE name = 'FOOTBALL(red)';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 4, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_FOOTBALL_UNIFORM4_optimized.jpg'
FROM goods WHERE name = 'FOOTBALL(red)';

-- Images for CANGEL KEY RING
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 1, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_KEYRING_1_optimized.JPG'
FROM goods WHERE name = 'CANGEL KEY RING(10cm)';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 2, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_KEYRING_2_optimized.JPG'
FROM goods WHERE name = 'CANGEL KEY RING(10cm)';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 3, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_KEYRING_3_optimized.JPG'
FROM goods WHERE name = 'CANGEL KEY RING(10cm)';

-- Images for PIN BUTTON
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 1, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_PINBUTTON_1_optimized.JPG'
FROM goods WHERE name = 'PIN BUTTON';


-- Images for TIE
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 1, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_TIE1_optimized.jpg'
FROM goods WHERE name = 'TIE';
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 2, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_TIE2_optimized.jpg'
FROM goods WHERE name = 'TIE';

-- Images for TATOO STICKER
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 1, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_TATOOSTICKER_optimized.jpg'
FROM goods WHERE name = 'TATOO STICKER';

-- Images for i CAP
INSERT INTO goods_detail_images (goods_id, image_order, image_url)
SELECT id, 1, 'https://d9are2p0j0wzf.cloudfront.net/MD/MD_ETC_1_optimized.jpg'
FROM goods WHERE name = 'i CAP';

--pubAdmin 계정 생성
INSERT INTO pub_admin(admin_id, department, password, booth_id) VALUES
                                                                    ('pubadmin1', '총', '$2a$10$ClTPXu2uIjuK1BKB5J2okOtlN/qlYhf3c/gaaLuHHyCc0KxCPJX3C', 1),
                                                                    ('pubadmin2', '자', '$2a$10$gt3s4ywXHlWF1ZpuSvagO.FCDNyFoCPww4PgFy.i1jcKr45TRk8pq', 2),
                                                                    ('pubadmin3', '경', '$2a$10$MqLnRYW4KnfgAajcWcRLzOaeKJ7Sb6bdNyNl.ZMRHGe5OQqrF1hty', 3),
                                                                    ('pubadmin4', '편', '$2a$10$50N7B.etktgFuA5RmDpusOE7C0cfjhLfz7/n955Wu1inCZTTSm1KK', 4),
                                                                    ('pubadmin5', '사', '$2a$10$PzNZ2HAzT0CdsfxrklhFXOfIug2Swybcpp.LKKWuvUefZ9iROliNS', 5),
                                                                    ('pubadmin6', '맛', '$2a$10$K2i.Bamq31qKVzDjVNgTbeYNNAe0I.OHgLkKNz5ltrAnN1Mdrr7fy', 6),
                                                                    ('pubadmin7', '인공자전', '$2a$10$sWiFIgm/Pt2tKfHPNZ9HJ.V9P.UuDxVJ0InmwC8u3qXbnrz6qBGDq', 7),
                                                                    ('pubadmin8', '컴공', '$2a$10$yVxwUuj7FObPFy119lSA/ewFfFgR.vlANK1T7YnxD7d7qC9Ni1bpa', 8),
                                                                    ('pubadmin9', '상', '$2a$10$NQX3cOwRYdFPR.OGgUMC3.g8g58vJDp1KUVcdcRnaVGOAWxIaOQlS', 9),
                                                                    ('pubadmin10', '문', '$2a$10$.IIzEoWoft//V3FEqIA4n.JWr3SDMlOHVWq96YmK94xDjl8Ufeba6', 10),
                                                                    ('pubadmin11', '공', '$2a$10$5MNVtBA/lUdDpSOyoik8fOn.e/0gDvyhO82Lz4t41N8167YNd2coS', 11),
                                                                    ('pubadmin12', 'H.U.G', '$2a$10$JrchIGnJ7qU0eRGYyuhMAOroc1HqU02ayNn9xYvls.Q8wDcDBwr3C', 12),
                                                                    ('pubadmin13', 'EXPANDED', '$2a$10$TxTUtyAghXzbLBiL0EHxzuMkCDOsQ24WPM2BZ5daqRQKrH3QAVf2i', 13);
INSERT INTO booth_operating_days (booth_id, operating_days)
SELECT
    id, -- 'pub' 타입인 부스의 ID
    'WED'-- 모든 행에 동일하게 삽입할 값
FROM
    booth
WHERE
    category= 'FOOD_TRUCK';




--event 더미로 우선 채우기
INSERT INTO event_detail_images (image_order, event_id, image_url)
select
    1,
    id,
    'https://d9are2p0j0wzf.cloudfront.net/%EA%B0%80%EB%82%98%EB%94%94/KakaoTalk_20250703_164910895.png'
from event;



select * from booth where booth_type='제휴';


