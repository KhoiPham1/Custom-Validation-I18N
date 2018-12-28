package com.codegym.controller;

import com.codegym.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("user")
public class LoginController {
    @ModelAttribute("user")
    public User setUpUserForm(){
        return new User();
    }

    @GetMapping("/login")
    public String Index(@CookieValue(value = "setUser",defaultValue = "") String setUser, Model model){
        // bắt buộc phải trả về 1 cookieValue để liên kết với view. nếu không có thì th:value="${cookieValue.value} sẽ lỗi
        Cookie cookie = new Cookie("setUser",setUser);
        model.addAttribute("cookieValue",cookie);
        return "login";
    }

    @PostMapping("/dologin")
    public String doLogin(@ModelAttribute("user")User user, Model model, @CookieValue(value = "setValue",defaultValue = "")String setUser, HttpServletResponse response, HttpServletRequest request){
        //implement business logic
        if (user.getEmail().equals("admin@gmail.com")&& user.getPassword().equals("12345")){
            if (user.getEmail()!=null){
                setUser = user.getEmail();
            }
            //create cookie and set it in respone
            Cookie cookie = new Cookie("setUser",setUser);
            //setMaxAge để thiết lập thời gian cho cookie, cũng là để xóa cookie khi đặt setMaxAge = 0
            cookie.setMaxAge(24*60*60);
            response.addCookie(cookie);

            //get all cookies
            Cookie[]cookies = request.getCookies();
            //iterate each cookie
            for (Cookie ck:cookies){
                //display only the cookie with the name 'setUser'
                if (ck.getName().equals("setUser")){
                    model.addAttribute("cookieValue",ck);
                    break;
                }else {
                    ck.setValue("");
                    model.addAttribute("cookieValue",ck);
                    break;
                }
            }
            model.addAttribute("message","Login success. Welcome");
        }else {
            user.setEmail("");
            Cookie cookie = new Cookie("setUser",setUser);
            model.addAttribute("cookieValue",cookie);
            model.addAttribute("message","Login failed. Try again.");
        }
        return "login";
    }
}
