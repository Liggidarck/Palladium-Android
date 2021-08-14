package com.george.vector.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.george.vector.auth.ui.theme.BluePrimary
import com.george.vector.auth.ui.theme.PalladiumTheme
import com.george.vector.users.admin.main.MainAdminActivity
import com.george.vector.users.caretaker.main.MainCaretakerActivity
import com.george.vector.users.executor.main.MainExecutorActivity
import com.george.vector.users.root.main.RootMainActivity
import com.george.vector.users.user.main.MainUserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.Modifier

class LoadingComposeActivity : ComponentActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    private val tag: String = "LoadingCompActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PalladiumTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        StartAnimation()
                    }

                }
            }

        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


    }

    @Composable
    fun StartAnimation() {
        CircularProgressIndicator(color = BluePrimary)
        GetUserAndStartApp()
    }


    @Composable
    fun GetUserAndStartApp() {
        val context = LocalContext.current
        val startRoot = Intent(context, RootMainActivity::class.java)
        val startZavhoz = Intent(context, MainCaretakerActivity::class.java)
        val startAdmin = Intent(context, MainAdminActivity::class.java)
        val startUser = Intent(context, MainUserActivity::class.java)
        val startExecutor = Intent(context, MainExecutorActivity::class.java)
        val startLogin = Intent(context, ActivityLogin::class.java)

        if(auth.currentUser != null) {
            val userID: String = auth.currentUser!!.uid
            Log.i("ComposeActivity", "user id: $userID")

            val documentReference: DocumentReference = firestore
                .collection("users")
                .document(userID)

            documentReference.addSnapshotListener{ snapshot, _ ->
                val role: String? = snapshot?.getString("role")
                val email: String? = snapshot?.getString("email")
                val permission: String? = snapshot?.getString("permission")
                Log.i(tag, "permission: $permission")
                Log.i(tag, "check_email: $email")
                Log.i(tag, "check_role: $role")

                if(role == "Root")
                    context.startActivity(startRoot)

                if(role == "Завхоз") {
                    startZavhoz.putExtra("permission", permission)
                    context.startActivity(startZavhoz)
                }

                if(role == "Администратор") {
                    startAdmin.putExtra("permission", permission)
                    context.startActivity(startAdmin)
                }

                if(role == "Пользователь") {
                    startUser.putExtra("email", email)
                    startUser.putExtra("permission", permission)
                    context.startActivity(startUser)
                }

                if(role == "Исполнитель") {
                    startExecutor.putExtra("email", email)
                    context.startActivity(startExecutor)
                }
            }

        } else
            context.startActivity(startLogin)
    }

    @Preview
    @Composable
    fun ComposablePreview() {
        StartAnimation()
    }

}