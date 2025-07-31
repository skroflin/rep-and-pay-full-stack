/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.utils.jwt;

import javax.crypto.SecretKey;

/**
 *
 * @author skroflin
 */
public class JwtTokenUtil {
    private String secret;
    private long expiration;
    private SecretKey signingKey;
}
