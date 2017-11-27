package br.com.rehem.rodrigo.controlepatrimonial.service;

import br.com.rehem.rodrigo.controlepatrimonial.config.JHipsterProperties;
import br.com.rehem.rodrigo.controlepatrimonial.domain.Movimentacao;
import br.com.rehem.rodrigo.controlepatrimonial.domain.User;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;



import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);
    
    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);
        
        sendEmail(to,jHipsterProperties.getMail().getFrom(), subject, content, isMultipart, isHtml);
    }

    @Async
    public void sendEmail(String to,String from, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        sendEmail(to,jHipsterProperties.getMail().getFrom(),null, subject, content, isMultipart, isHtml);
    }
    
    @Async
    public void sendEmail(String to,String from, String cc, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to.split(";"));
            message.setFrom(from);
            if(cc != null)
            {	
            	message.setCc(cc.split(";"));
            }
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }
    
    @Async
    public void sendActivationEmail(User user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendMovimentacaoCopatEmail(User user, Movimentacao movimentacao) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        
        String emailDestinatario = messageSource.getMessage("email.copat.destinatario", null, locale);
        String emailComCopia = messageSource.getMessage("email.copat.cc", null, locale);
        log.debug("Envio de e-mail com Movimentação  to '{}'", emailDestinatario);
        
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable("movimentacao", movimentacao);
        String content = "";
        if(movimentacao.getTipoMovimentacao().getId() == 1)
        {
        	content = templateEngine.process("movimentacaoEntregaCopat", context);
        }else
        {
        	content = templateEngine.process("movimentacaoDevolucaoCopat", context);
        }
        
        String[] argSubject = new String[4];
        
        argSubject[0] = movimentacao.getTipoMovimentacao().getNome();
        argSubject[1] = movimentacao.getPessoa().getCategoriaFuncional().toString();
        argSubject[2] = movimentacao.getPessoa().getNome();
        argSubject[3] = movimentacao.getPessoa().getCadastro().toString();
        
        String subject = messageSource.getMessage("email.copat.subject",argSubject , locale);
        sendEmail(emailDestinatario+";"+user.getEmail(),user.getEmail(),emailComCopia, subject, content, false, true);
    }
    
    @Async
    public void sendCreationEmail(User user, String baseUrl) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("creationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }
    
}
