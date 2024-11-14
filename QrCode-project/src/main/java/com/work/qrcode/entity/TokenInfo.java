package com.work.qrcode.entity;

import lombok.Data;

/**
 * TokenInfo
 *
 */
@Data
public class TokenInfo {

	/**
	 * 令牌值
	 */
	private String token;

	/**
	 * 过期秒数
	 */
	private int expire;

}
