package it.polito.mad.carpooling


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var textNotify: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        this.textNotify = findViewById(R.id.NotifyError)

        //Configure Button Google Sign
        val signInButton : SignInButton  = findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        signInButton.setOnClickListener{
            SignGoogle()
        }
    }
    private fun SignGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        textNotify.text = ""
        val currentUser = auth.currentUser
        if(currentUser == null){
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            if (currentUser != null) {
                intent.putExtra("name",currentUser.displayName)
                intent.putExtra("email", currentUser.email )
                startActivity(intent)
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                textNotify.text = "Google sign in failed"
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    var isNewUser : Boolean? = task.getResult()?.additionalUserInfo?.isNewUser()

                    //If is new user register so save in firestore database
                    if(isNewUser == true) {
                        val newUser = hashMapOf(
                            "fullName" to user.displayName,
                            "nickname" to "",
                            "email" to user.email,
                            "location" to "",
                            "image" to "images/users/user1",
                            "identifier" to user.uid

                        )
                        val db = FirebaseFirestore.getInstance()
                        db.collection("users").add(newUser)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                    }
                    val intent = Intent(this,MainActivity::class.java)
                    intent.putExtra("identifierUser",user.uid)

                    startActivity(intent)


                } else {
                    textNotify.text = "Error Signing With Google"
                }
            }
    }



    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }



}