package com.sxt.sys.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


public class VerificationCodeUtil extends HttpServlet {
    private static final int WIDTH = 100;//设置验证码图片宽度
    private static final int HEIGHT = 40;//设置验证码图片高度
    private static final int LENGTH = 4;//设置验证码长度
    //设置验证码随机出现的字符
    private static final String str = "1234567890" +"abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static char[] chars = str.toCharArray();//将字符放在数组中方便随机读取
    private static Random random = new Random();



    //生成验证码
    public static BufferedImage getCheckCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置输出的类型为图片
        response.setContentType("image/jpeg");

        //设置不进行缓存
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

        //画笔
        Graphics graphics = image.getGraphics();

        //设置背景颜色并绘制矩形背景
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        //用于记录生成的验证码
        String code ="";

        //生成字符验证码并绘制
        for (int i = 0; i < LENGTH; i++) {
            String c = "" + chars[random.nextInt(str.length())];
            graphics.setColor(getColor());
            // 设置字体
            graphics.setFont(new Font("隶书", Font.BOLD, 25));
            // 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
            graphics.drawString(c, 20 * i + 10, 28);
            code += c;
        }
        //
        System.out.println("验证码  "+code);
        //生成干扰点
        for (int i = 0; i < 100; i++) {
            graphics.setColor(getColor());
            graphics.drawOval(random.nextInt(100), random.nextInt(50), 1, 1);
        }

        //将生成的验证码存入session中，以便进行校验
        HttpSession session = request.getSession();
        session.setAttribute("code", code);

        //绘制图片
        graphics.dispose();
        return image;

        //将图片输出到response中
//        ImageIO.write(image, "JPEG", response.getOutputStream());

    }

    //随机生成颜色
    private static Color getColor() {
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }


}
