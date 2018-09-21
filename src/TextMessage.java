import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class TextMessage {

    private static String USER_NAME = "temptest22345@gmail.com";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "TempTest123!"; // GMail password
    private static String RECIPIENT;
    String subject = "TEMP EXCEEDED";
    String body = "TEMP EXCEEDED";



    public TextMessage() {

    }

    public void setRECIPIENT(String phoneNumber, int provider){
        String sendto = phoneNumber+"@";
        if(provider==1){
            sendto += "txt.att.net";
        }else if (provider ==2){
            sendto += "tmomail.net";
        }else if (provider ==3){
            sendto += "vtext.com";
        }else if (provider ==4){
            sendto += "messaging.sprintpcs.com";
        }
        RECIPIENT = sendto;
    }




    public void sendMail() {
        String[] to = {RECIPIENT};
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", USER_NAME);
        props.put("mail.smtp.password", PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(USER_NAME));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, USER_NAME, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
