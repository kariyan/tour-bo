package com.wemakeprice.tour.bo.common.enumtype;

public enum PrivacyGrade {

    P1("P1", 1, "계좌번호"),
    P2("P2", 1, "주민등록번호"),
    P3("P3", 1, "운전면허번호"),
    P4("P4", 1, "여권번호"),
    P5("P5", 1, "신용카드번호"),
    P6("P6", 1, "개인위치정보"),
    P7("P7", 2, "휴대폰번호"),
    P8("P8", 2, "이메일주소"),
    P90("P90", 3, "이름(한글,영문), 생년월일, 성별, 유선 전화번호, 주소, 우편번호, 아 이디, 개인통관고유번호, 아이핀번호, DI, CI, 은행명, 예금주명, 현금 영수증 카드번호, 상호명, 사업자등록번호, 대학교명, 프로필이미지"),
    P91("P91", 99, "MAC, OS정보, 브라우저 정보, 쿠키, 접속기록, IP, 이통사명, 단말기 정보, 단말기 OS 정보, 국가명, 페이스북 UID, 사물위치정보");

    String key;
    int grade;
    String description;

    PrivacyGrade(String key, int grade, String description) {
        this.key = key;
        this.grade = grade;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
