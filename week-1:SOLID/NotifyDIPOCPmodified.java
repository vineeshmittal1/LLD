import java.util.*;

interface MailService{
  void send(String templ, String to, String body);
  
}
interface SmsService{
   void sendOTP(String phone, String code);
   
}

class SmtpMailer implements MailService{
  @Override
  public void send(String templ, String to, String body) {
        System.out.println("[SMTP] template=" + templ + " to=" + to + " body=" + body);
    }
}

class TwilioClient implements SmsService{
  @Override
  public void sendOTP(String phone, String code) {
        System.out.println("[Twilio] OTP " + code + " -> " + phone);
    }
}

class User{
  String email;
  String phone;
  User(String email,String phone){
    this.email=email;
    this.phone=phone;
  }
}

class SignUpService {
  
   private final MailService mailer;
   private final SmsService sms;
   
   SignUpService(MailService mailer,SmsService sms){
     this.mailer=mailer;
     this.sms=sms;
   }
    boolean signUp(User u){
        if (u.email == null || u.email.isEmpty()) return false;
        mailer.send("welcome", u.email, "Welcome!");
        sms.sendOTP(u.phone, "123456");
        return true;
    }
}
public class NotifyDIPOCPmodified {
    public static void main(String[] args) {
      MailService mailer=new SmtpMailer();
      SmsService sms=new TwilioClient();
      SignUpService svc = new SignUpService(mailer,sms);
      svc.signUp(new User("user@example.com", "+15550001111"));
    }
}