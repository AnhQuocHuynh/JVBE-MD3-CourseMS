package config;


import config.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        // ── BƯỚC 1: ĐỌC HEADER AUTHORIZATION
        // request.getHeader("Authorization") = đọc giá trị của header "Authorization" từ request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        // Nếu không có header Authorization HOẶC header không bắt đầu bằng "Bearer " →
        // Không có token → Cho đi qua luôn, không check.
        // Spring Security sẽ chặn lại ở tầng sau nếu endpoint đó yêu cầu phải đăng nhập.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // ── BƯỚC 2: CẮT LẤY CHUỖI TOKEN THUẦN
        // .substring(7) = cắt 7 ký tự đầu ("Bearer " có 7 ký tự kể cả dấu cách) để lấy phần token
        jwt = authHeader.substring(7);
        // ── BƯỚC 3: TRÍCH XUẤT EMAIL TỪ TOKEN
        // Gọi JwtService để giải mã token và lấy ra "subject" = email mà chúng ta đã nhét vào lúc tạo token
        userEmail = jwtService.extractUsername(jwt);
        // ── BƯỚC 4: XÁC THỰC VÀ ĐĂNG KÝ VÀO SECURITY CONTEXT
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // ── BƯỚC 5: CHO REQUEST ĐI TIẾP
        filterChain.doFilter(request, response);
    }
}
