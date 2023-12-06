package io.kontur.keycloak.listener;

import org.jboss.logging.Logger;
import org.keycloak.email.DefaultEmailSenderProvider;
import org.keycloak.email.EmailException;
import org.keycloak.email.EmailTemplateProvider;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class KonturEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(KonturEventListenerProvider.class);

    private final KeycloakSession session;
    private final RealmProvider model;

    public KonturEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event) {

        if (EventType.VERIFY_EMAIL.equals(event.getType())) {
            RealmModel realm = this.model.getRealm(event.getRealmId());
            UserModel user = this.session.users().getUserById(realm, event.getUserId());

            EmailTemplateProvider emailTemplateProvider = session.getProvider(EmailTemplateProvider.class);

            List<Object> subjectAttr = new ArrayList<>();
            subjectAttr.add(0, realm.getName());
            subjectAttr.add(1, user.getFirstName());

            Map<String, Object> bodyAttr = new HashMap<>();
            bodyAttr.put("realmName", realm.getName());
            bodyAttr.put("user", user.getFirstName());

            user.addRequiredAction(UserModel.RequiredAction.VERIFY_EMAIL);

            try {
                emailTemplateProvider
                    .setRealm(realm)
                    .setUser(user)
                    .send("welcomeEmailSubject",
                          subjectAttr,
                          "event-verify_email.ftl",
                          bodyAttr);

            } catch (EmailException e) {
                log.error("Failed to send email", e);
            }
        }

    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }
}
