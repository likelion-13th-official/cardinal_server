-- ========================================
-- 1️⃣ 테이블 초기화 (FK 순서 고려)
-- ========================================

-- 1. 테이블 초기화 (FK 참조 때문에 CASCADE 적용)
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

-- ========================================
-- 2️⃣ Users 데이터 삽입
-- ========================================

INSERT INTO users (nickname, profile_image_url) VALUES
('멋쟁이토마토', 'https://example.com/profile1.jpg'),
('해피코더', 'https://example.com/profile2.jpg'),
('코딩마법사', 'https://example.com/profile3.jpg'),
('개발왕김개발', 'https://example.com/profile4.jpg'),
('알고리즘귀재', 'https://example.com/profile5.jpg'),
('데이터분석가', 'https://example.com/profile6.jpg'),
('자바신', 'https://example.com/profile7.jpg'),
('파이썬사랑', 'https://example.com/profile8.jpg'),
('리액트장인', 'https://example.com/profile9.jpg'),
('뷰마스터', 'https://example.com/profile10.jpg'),
('노드고수', 'https://example.com/profile11.jpg'),
('스프링달인', 'https://example.com/profile12.jpg'),
('장고천재', 'https://example.com/profile13.jpg'),
('플러터개발자', 'https://example.com/profile14.jpg'),
('스위프트마스터', 'https://example.com/profile15.jpg'),
('코틀린사랑', 'https://example.com/profile16.jpg'),
('게임개발자', 'https://example.com/profile17.jpg'),
('머신러닝전문가', 'https://example.com/profile18.jpg'),
('딥러닝연구하는 유이', 'https://example.com/profile19.jpg'),
('머신러닝 공부하는 애은이', 'https://example.com/profile20.jpg');

-- ========================================
-- 3️⃣ Map 데이터 삽입
-- ========================================

INSERT INTO map (position, latitude, longitude) VALUES
('학생회관 앞', 37.5833, 127.0001),
('중앙광장', 37.5838, 127.0005),
('도서관 옆 잔디밭', 37.5842, 126.9998),
('공학관 1층 로비', 37.5829, 127.0010),
('인문관 벤치', 37.5835, 126.9990),
('대운동장', 37.5850, 127.0020),
('정문', 37.5820, 126.9980),
('후문', 37.5860, 127.0030),
('기숙사 식당 앞', 37.5810, 127.0015),
('체육관 입구', 37.5845, 127.0018);

-- ========================================
-- 4️⃣ Booth 데이터 삽입
-- ========================================

 INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, day_of_week, start_time, end_time, is_operating, notice, instagram_url, menu_image_url, table_layout_url,notice_updated_at) VALUES
 ('PubBooth', '주점', '코딩 주점', 6, '코딩하며 즐기는 신개념 주점!', 'https://example.com/booth1.jpg', 150, '금', '2025-08-16 18:00:00', '2025-08-17 02:00:00', true, '신분증 필수 지참!', 'https://instagram.com/coding_pub', 'https://example.com/pub_menu1.jpg', 'https://example.com/pub_layout1.jpg','2025-08-18 22:00:00'),
 ('PubBooth', '주점', '개발자들의 쉼터', 6, '지친 개발자들을 위한 힐링 공간', 'https://example.com/booth2.jpg', 250, '금', '2025-08-16 17:00:00', '2025-08-17 01:00:00', true, '외부 안주 반입 금지', 'https://instagram.com/dev_rest', 'https://example.com/pub_menu2.jpg', 'https://example.com/pub_layout2.jpg','2025-08-18 22:00:00'),
 ('PubBooth', '주점', '알고리즘 파티', 6, '알고리즘 문제 풀고, 술도 마시고!', 'https://example.com/booth3.jpg', 180, '상시', '2025-08-17 19:00:00', '2025-08-18 03:00:00', true, '팀 대항전 이벤트 진행!', 'https://instagram.com/algo_party', 'https://example.com/pub_menu3.jpg', 'https://example.com/pub_layout1.jpg','2025-08-18 22:00:00'),
 ('PubBooth', '주점', '버그 사냥꾼', 6, '버그 잡고 스트레스 풀자!', 'https://example.com/booth4.jpg', 320, '상시', '2025-08-17 18:00:00', '2025-08-18 02:00:00', true, '현금 결제만 가능합니다', 'https://instagram.com/bug_hunter', 'https://example.com/pub_menu4.jpg', 'https://example.com/pub_layout4.jpg','2025-08-18 22:00:00'),
 ('PubBooth', '주점', '해커들의 밤', 6, '해커톤보다 재미있는 해커들의 주점', 'https://example.com/booth5.jpg', 450, '상시', '2025-08-18 20:00:00', '2025-08-19 04:00:00', false, '일요일은 쉽니다', 'https://instagram.com/hackers_night', 'https://example.com/pub_menu5.jpg', 'https://example.com/pub_layout1.jpg','2025-08-18 22:00:00');

 -- FoodTruckBooth (5)
 INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, day_of_week, start_time, end_time, is_operating, menu_image_url) VALUES
 ('FoodTruckBooth', '푸드트럭', '코딩푸드', 6, '코더들을 위한 맛있는 간식!', 'https://example.com/booth6.jpg', 500, '금', '2025-08-16 12:00:00', '2025-08-16 22:00:00', true, 'https://example.com/food_menu1.jpg'),
 ('FoodTruckBooth', '푸드트럭', '개발자 핫도그', 7, '세상에서 제일 맛있는 핫도그', 'https://example.com/booth7.jpg', 380, '금', '2025-08-16 11:00:00', '2025-08-16 21:00:00', true, 'https://example.com/food_menu2.jpg'),
 ('FoodTruckBooth', '푸드트럭', '알고리즘 츄러스', 8, '달콤한 츄러스와 함께하는 알고리즘', 'https://example.com/booth8.jpg', 420, '상시', '2025-08-17 13:00:00', '2025-08-17 23:00:00', true, 'https://example.com/food_menu3.jpg'),
 ('FoodTruckBooth', '푸드트럭', '버그와플', 9, '버그처럼 달콤한 와플', 'https://example.com/booth9.jpg', 280, '상시', '2025-08-17 12:00:00', '2025-08-17 22:00:00', true, 'https://example.com/food_menu4.jpg'),
 ('FoodTruckBooth', '푸드트럭', '해커스테이크', 10, '해커처럼 강력한 스테이크', 'https://example.com/booth10.jpg', 600, '상시', '2025-08-18 14:00:00', '2025-08-18 23:00:00', true, 'https://example.com/food_menu5.jpg');

 -- YardBooth (5)
 INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, day_of_week, start_time, end_time, is_operating) VALUES
 ('YardBooth', '마당사업', '코딩 체험 부스', 2, '누구나 쉽게 배우는 코딩!', 'https://example.com/booth11.jpg', 700, '금', '2025-08-16 10:00:00', '2025-08-16 18:00:00', true),
 ('YardBooth', '마당사업', '개발자 플리마켓', 3, '개발자들의 애장품을 만나보세요', 'https://example.com/booth12.jpg', 450, '상시', '2025-08-17 11:00:00', '2025-08-17 17:00:00', true),
 ('YardBooth', '마당사업', '알고리즘 경진대회', 6, '최고의 알고리즈머는 누구?', 'https://example.com/booth13.jpg', 800, '상시', '2025-08-17 13:00:00', '2025-08-17 16:00:00', true),
 ('YardBooth', '마당사업', '버그찾기 이벤트', 2, '버그 찾고 상품 받자!', 'https://example.com/booth14.jpg', 650, '상시', '2025-08-18 14:00:00', '2025-08-18 17:00:00', true),
 ('YardBooth', '마당사업', '오픈소스 컨퍼런스', 4, '오픈소스에 대한 모든 것', 'https://example.com/booth15.jpg', 900, '상시', '2025-08-18 10:00:00', '2025-08-18 18:00:00', true);

 -- PhotoBooth (5)
 INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, day_of_week, start_time, end_time, is_operating) VALUES
 ('PhotoBooth', '포토부스', '인생샷 연구소', 5, '개발자 컨셉의 인생샷을 찍어보세요', 'https://example.com/booth16.jpg', 1200, '금', '2025-08-16 10:00:00', '2025-08-18 22:00:00', true),
 ('PhotoBooth', '포토부스', '코딩네컷', 7, '코딩과 함께하는 네컷사진', 'https://example.com/booth17.jpg', 1500, '금', '2025-08-16 10:00:00', '2025-08-18 22:00:00', true),
 ('PhotoBooth', '포토부스', '알고리즘 스튜디오', 8, '알고리즘처럼 완벽한 사진', 'https://example.com/booth18.jpg', 1300, '상시', '2025-08-17 10:00:00', '2025-08-18 22:00:00', true),
 ('PhotoBooth', '포토부스', '버그 포토존', 9, '재미있는 버그 컨셉의 포토존', 'https://example.com/booth19.jpg', 1100, '상시', '2025-08-17 10:00:00', '2025-08-18 22:00:00', true),
 ('PhotoBooth', '포토부스', '해커 스냅', 10, '해커처럼 멋진 스냅사진', 'https://example.com/booth20.jpg', 1800, '상시', '2025-08-18 10:00:00', '2025-08-18 22:00:00', true);

 -- PartnershipBooth (10)
 INSERT INTO booth (booth_type, category, name, location_id, description, thumbnail_url, view_count, day_of_week, start_time, end_time, is_operating, logo_image_url) VALUES
 ('PartnershipBooth', '제휴', '멋쟁이사자처럼', 4, '멋쟁이사자처럼과 함께하는 이벤트', 'https://example.com/booth21.jpg', 2000, '금', '2025-08-16 10:00:00', '2025-08-18 18:00:00', true, 'https://example.com/logo_likelion.png'),
 ('PartnershipBooth', '제휴', 'Google', 4, 'Google이 준비한 특별한 선물', 'https://example.com/booth22.jpg', 2500, '금', '2025-08-16 10:00:00', '2025-08-18 18:00:00', true, 'https://example.com/logo_google.png'),
 ('PartnershipBooth', '제휴', 'Microsoft', 4, 'Microsoft와 함께하는 코딩 워크샵', 'https://example.com/booth23.jpg', 2200, '상시', '2025-08-17 10:00:00', '2025-08-18 18:00:00', true, 'https://example.com/logo_ms.png'),
 ('PartnershipBooth', '제휴', 'Amazon Web Services', 4, 'AWS 크레딧과 다양한 굿즈 증정', 'https://example.com/booth24.jpg', 2800, '상시', '2025-08-17 10:00:00', '2025-08-18 18:00:00', true, 'https://example.com/logo_aws.png'),
 ('PartnershipBooth', '제휴', '네이버', 5, '네이버 개발자들과의 만남', 'https://example.com/booth25.jpg', 3000, '상시', '2025-08-18 10:00:00', '2025-08-18 18:00:00', true, 'https://example.com/logo_naver.png'),
 ('PartnershipBooth', '제휴', '카카오', 5, '카카오가 준비한 특별한 이벤트', 'https://example.com/booth26.jpg', 3200, '상시', '2025-08-18 10:00:00', '2025-08-18 18:00:00', true, 'https://example.com/logo_kakao.png'),
 ('PartnershipBooth', '제휴', '배달의민족', 5, '배달의민족과 함께하는 맛있는 축제', 'https://example.com/booth27.jpg', 2900, '금', '2025-08-16 10:00:00', '2025-08-18 18:00:00', true, 'https://example.com/logo_baemin.png'),
 ('PartnershipBooth', '제휴', '토스', 5, '토스가 준비한 금융 퀴즈 이벤트', 'https://example.com/booth28.jpg', 2600, '상시', '2025-08-17 10:00:00', '2025-08-18 18:00:00', true, 'https://example.com/logo_toss.png'),
 ('PartnershipBooth', '제휴', '당근마켓', 4, '당근마켓과 함께하는 중고거래 체험', 'https://example.com/booth29.jpg', 2300, '상시', '2025-08-18 10:00:00', '2025-08-18 18:00:00', true, 'https://example.com/logo_daangn.png'),
 ('PartnershipBooth', '제휴', '쿠팡', 4, '쿠팡이 준비한 로켓배송 이벤트', 'https://example.com/booth30.jpg', 3500, '금', '2025-08-16 10:00:00', '2025-08-18 18:00:00', true, 'https://example.com/logo_coupang.png');


 -- 4. Menus (20 records for Booths)
 INSERT INTO menus (booth_id, name, price) VALUES
 (9, '코딩맥주', 5000),
 (10, '개발자칵테일', 7000),
 (20, '힐링소주', 4000),
 (12, '위로막걸리', 6000),
 (13, '알고리즘하이볼', 8000),
 (14, '디버깅사이다', 2000),
 (14, '컴파일콜라', 2000),
 (15, '해커에너지드링크', 3000),
 (16, '타코야끼', 6000),
 (16, '닭꼬치', 4000),
 (17, '클래식핫도그', 3500),
 (17, '치즈핫도그', 4500),
 (18, '초코츄러스', 3000),
 (18, '아이스크림츄러스', 4500),
 (19, '딸기와플', 5000),
 (19, '누텔라와플', 5500),
 (10, '부채살스테이크', 12000),
 (10, '감자튀김', 5000),
 (11, '파이썬 키링 만들기', 5000),
 (12, '개발자 굿즈', 10000);

ALTER TABLE event DROP CONSTRAINT IF EXISTS event_location_id_key;

 -- 5. Events (10 records)
-- 1. event 테이블 처리
-- =========================

INSERT INTO event (name, location_id, description, thumbnail_url, view_count, day_of_week, start_time, end_time, is_operating) VALUES
('DJ 파티', 6, '코딩보다 신나는 DJ 파티!', 'https://example.com/event3.jpg', 6000, '금', '2025-08-16 20:00:00', '2025-08-17 00:00:00', true),
('보물찾기 이벤트', 2, '축제 곳곳에 숨겨진 보물을 찾아라!', 'https://example.com/event4.jpg', 7000, '상시', '2025-08-17 10:00:00', '2025-08-17 18:00:00', true),
('코스프레 대회', 6, '최고의 개발자 코스프레는 누구?', 'https://example.com/event5.jpg', 5500, '상시', '2025-08-17 14:00:00', '2025-08-17 16:00:00', true),
('네트워킹 파티', 1, '다양한 개발자들과 교류할 수 있는 기회', 'https://example.com/event6.jpg', 6500, '상시', '2025-08-17 18:00:00', '2025-08-17 20:00:00', true),
('경품 추첨', 2, '축제의 마지막을 장식할 경품 추첨', 'https://example.com/event7.jpg', 8000, '상시', '2025-08-18 16:00:00', '2025-08-18 17:00:00', true),
('폐막식', 6, '아쉬운 축제의 마무리', 'https://example.com/event8.jpg', 4000, '상시', '2025-08-18 18:00:00', '2025-08-18 20:00:00', true),
('인디밴드 공연', 3, '감성적인 인디밴드의 라이브 공연', 'https://example.com/event9.jpg', 3000, '금', '2025-08-16 17:00:00', '2025-08-16 19:00:00', true),
('마술쇼', 5, '코딩처럼 신기한 마술의 세계', 'https://example.com/event10.jpg', 3500, '상시', '2025-08-17 13:00:00', '2025-08-17 14:00:00', true);


 -- 6. Event Detail Images (10 records)
 INSERT INTO event_detail_images (image_order,event_id, image_url) VALUES
 (1,1, 'https://example.com/event1_detail1.jpg'),
 (2,1, 'https://example.com/event1_detail2.jpg'),
 (3,2, 'https://example.com/event2_detail1.jpg'),
 (4,3, 'https://example.com/event3_detail1.jpg'),
 (5,4, 'https://example.com/event4_detail1.jpg'),
 (6,5, 'https://example.com/event5_detail1.jpg'),
 (7,6, 'https://example.com/event6_detail1.jpg'),
 (8,7, 'https://example.com/event7_detail1.jpg'),
 (9,8, 'https://example.com/event8_detail1.jpg');


 -- 7. Goods (10 records)


INSERT INTO goods (name, description, thumbnail_url, view_count, location_id) VALUES
('개발자 티셔츠', 'I speak fluent Java', 'https://example.com/goods1.jpg', 1500, 6),
('코딩 머그컵', 'Keep Calm and Code On', 'https://example.com/goods2.jpg', 2000, 6),
('알고리즘 노트', '알고리즘 문제 풀이에 최적화된 노트', 'https://example.com/goods3.jpg', 1800, 6),
('버그 인형', '스트레스 해소용 버그 인형', 'https://example.com/goods4.jpg', 2500, 6),
('해커 키보드', '해커처럼 빠른 타이핑을 위한 기계식 키보드', 'https://example.com/goods5.jpg', 3000, 6),
('개발자 스티커팩', '노트북을 꾸밀 수 있는 다양한 개발자 스티커', 'https://example.com/goods6.jpg', 2200, 6),
('코딩 양말', '코딩할 때 신으면 집중력이 올라가는 양말', 'https://example.com/goods7.jpg', 1700, 6),
('알고리즘 마우스패드', '알고리즘 문제 풀 때 유용한 마우스패드', 'https://example.com/goods8.jpg', 1900, 6),
('버그 스프레이', '버그를 한 방에 해결해주는 상상 속의 스프레이', 'https://example.com/goods9.jpg', 2800, 6),
('해커 후드티', '해커처럼 보이고 싶을 때 입는 후드티', 'https://example.com/goods10.jpg', 3200, 6);

 -- 8. Goods Detail Images (10 records)
INSERT INTO goods_detail_images (image_order,goods_id, image_url) VALUES
(1,1, 'https://example.com/goods1_detail1.jpg'),
 (2,2, 'https://example.com/goods2_detail1.jpg'),
 (3,3, 'https://example.com/goods3_detail1.jpg'),
 (4,4, 'https://example.com/goods4_detail1.jpg'),
 (5,5, 'https://example.com/goods5_detail1.jpg'),
 (6,6, 'https://example.com/goods6_detail1.jpg'),
 (7,7, 'https://example.com/goods7_detail1.jpg'),
 (8,8, 'https://example.com/goods8_detail1.jpg'),
 (9,9, 'https://example.com/goods9_detail1.jpg'),
 (10,10, 'https://example.com/goods10_detail1.jpg');

 -- 9. Performances (20 records)


INSERT INTO performance (category, name, date, description, thumbnail_url, view_count, location_id) VALUES
('동아리', '멋쟁이사자처럼 노래패', '2025-08-16 13:00:00', '멋쟁이사자처럼 멤버들이 부르는 감미로운 노래', 'https://example.com/perf1.jpg', 1200, 6),
('동아리', '코딩댄스팀', '2025-08-16 15:00:00', '코딩 동작을 응용한 신나는 댄스 공연', 'https://example.com/perf2.jpg', 1500, 6),
('동아리', '알고리즘 연극반', '2025-08-17 11:00:00', '알고리즘을 주제로 한 재미있는 연극', 'https://example.com/perf3.jpg', 1300, 6),
('동아리', '버그 버스킹', '2025-08-17 17:00:00', '버그 잡는 심정으로 부르는 애절한 발라드', 'https://example.com/perf4.jpg', 1800, 6),
('동아리', '해커 밴드', '2025-08-18 13:00:00', '해커들의 열정이 느껴지는 락밴드 공연', 'https://example.com/perf5.jpg', 2000, 6),
('동아리', '코딩 오케스트라', '2025-08-18 15:00:00', '코딩처럼 정교하고 아름다운 오케스트라 연주', 'https://example.com/perf6.jpg', 1600, 6),
('영화제', '개발자의 사랑', '2025-08-16 19:00:00', '개발자의 애틋한 사랑 이야기를 담은 단편 영화', 'https://example.com/perf7.jpg', 2500, 6),
('영화제', '알고리즘의 눈물', '2025-08-17 19:00:00', '알고리즘에 울고 웃는 개발자들의 이야기', 'https://example.com/perf8.jpg', 2200, 6),
('영화제', '버그와의 전쟁', '2025-08-18 19:00:00', '버그를 잡기 위한 개발자들의 처절한 사투', 'https://example.com/perf9.jpg', 2800, 6),
('영화제', '해커의 꿈', '2025-08-16 21:00:00', '세상을 바꾸고 싶은 해커의 이야기', 'https://example.com/perf10.jpg', 3000, 6),
('영화제', '코딩의 신', '2025-08-17 21:00:00', '전설적인 코더의 일대기를 그린 영화', 'https://example.com/perf11.jpg', 3200, 6),
('영화제', 'AI의 역습', '2025-08-18 21:00:00', '인공지능이 인류를 위협하는 SF 스릴러', 'https://example.com/perf12.jpg', 3500, 6),
('아티스트', '아이유', '2025-08-16 22:00:00', '국민 여동생 아이유의 특별 공연', 'https://example.com/perf13.jpg', 10000, 6),
('아티스트', '악동뮤지션', '2025-08-17 22:00:00', '악동뮤지션의 재치있는 무대', 'https://example.com/perf14.jpg', 9000, 6),
('아티스트', '10cm', '2025-08-18 22:00:00', '10cm의 감미로운 목소리', 'https://example.com/perf15.jpg', 8000, 6),
('아티스트', '볼빨간사춘기', '2025-08-16 18:00:00', '볼빨간사춘기의 상큼한 공연', 'https://example.com/perf16.jpg', 7000, 6),
('아티스트', '자이언티', '2025-08-17 18:00:00', '자이언티의 독특한 음색', 'https://example.com/perf17.jpg', 7500, 6),
('아티스트', '크러쉬', '2025-08-18 18:00:00', '크러쉬의 감성적인 R&B 무대', 'https://example.com/perf18.jpg', 8500, 6);
 -- 10. Scraps (10 records)



INSERT INTO scrap (user_id, content_id, content_type, created_at) VALUES
(1, 1, '부스', '2025-08-16 19:00:00'),
(1, 13, '공연', '2025-08-16 23:00:00'),
(2, 6, '부스', '2025-08-16 13:00:00'),
(2, 3, '이벤트', '2025-08-16 21:00:00'),
(3, 1, '굿즈', '2025-08-17 10:00:00'),
(4, 21, '부스', '2025-08-17 11:00:00'),
(5, 7, '공연', '2025-08-17 19:30:00'),
(6, 4, '이벤트', '2025-08-18 12:00:00'),
(7, 16, '부스', '2025-08-18 14:00:00'),
(8, 5, '굿즈', '2025-08-18 15:00:00');

-- ========================================
-- 11. Amenity 데이터 삽입
-- ========================================
INSERT INTO amenity (name, location_id) VALUES
('분리수거', 1),
('대피로', 2),
('간이테이블1', 3),
('간이테이블2', 5);