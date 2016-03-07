package in.learner.android.letuslearn;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by adithya on 7/3/16.
 */
public class details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        TextView name=(TextView)findViewById(R.id.profname);
        TextView phone=(TextView)findViewById(R.id.phone);
        final TextView email=(TextView)findViewById(R.id.email);
        ImageButton call=(ImageButton)findViewById(R.id.call);
        final ImageButton mail=(ImageButton)findViewById(R.id.mail);
        name.setText(getIntent().getExtras().getString("name"));
        phone.setText(getIntent().getExtras().getString("phone"));
        email.setText(getIntent().getExtras().getString("mail"));

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberToDial = "tel:" + getIntent().getExtras().getString("phone");
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(numberToDial)));
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[] { email.getText().toString() });
                Email.putExtra(Intent.EXTRA_SUBJECT, "Willing to attend classes " );
                Email.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(Email, "Send Email"));
            }
        });


    }
}
