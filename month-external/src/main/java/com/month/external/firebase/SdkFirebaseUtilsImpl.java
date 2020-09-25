package com.month.external.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.month.external.firebase.dto.CustomFirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class SdkFirebaseUtilsImpl implements FirebaseUtils {

	public SdkFirebaseUtilsImpl() {
		if (FirebaseApp.getApps().isEmpty()) {
			initializedFirebaseApp();
		}
	}

	private void initializedFirebaseApp() {
		try {
			InputStream serviceAccount = new ClassPathResource("firebase-admin.json").getInputStream();
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();
			FirebaseApp.initializeApp(options);
			log.info("파이어베이스 앱과의 연동이 완료되었습니다.");
		} catch (IOException e) {
			throw new IllegalArgumentException(String.format("파이어베이스와의 연동이 실패 하였습니다. (%s)", e));
		}
	}

	public CustomFirebaseToken getDecodedToken(String idToken) {
		try {
			FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
			return CustomFirebaseToken.of(firebaseToken);
		} catch (FirebaseAuthException e) {
			throw new IllegalArgumentException(String.format("잘못된 idToken (%s) 입니다.", idToken));
		}
	}

}
