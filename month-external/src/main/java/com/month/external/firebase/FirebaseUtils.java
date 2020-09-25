package com.month.external.firebase;

import com.month.external.firebase.dto.CustomFirebaseToken;

public interface FirebaseUtils {

	CustomFirebaseToken getDecodedToken(String idToken);

}
