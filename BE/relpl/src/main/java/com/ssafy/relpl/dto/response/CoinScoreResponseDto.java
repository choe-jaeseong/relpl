package com.ssafy.relpl.dto.response;

// 성공 dto
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinScoreResponseDto {

    private String coinEventDate; // 이벤트 발생 날짜. yyyy-MM-dd HH:mm

    private int coinAmount; // 단일 포인트 액수

    private String coinEventDetail; // 상세 내용 VARCHAR(200)

}