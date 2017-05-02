package uk.me.ajmfulcher.textwrap.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import uk.me.ajmfulcher.textwrap.TextWrapLayout;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextWrapLayout leftTextWrapLayout = (TextWrapLayout) findViewById(R.id.left_aligned_textWrapLayout);
		leftTextWrapLayout.setText(getText());
		TextWrapLayout rightTextWrapLayout = (TextWrapLayout) findViewById(R.id.right_aligned_textWrapLayout);
		rightTextWrapLayout.setText(getText());
		TextWrapLayout paddedLeftTextWrapLayout = (TextWrapLayout) findViewById(R.id.padded_left_textWrapLayout);
		paddedLeftTextWrapLayout.setText(getText());
		TextWrapLayout paddedRightTextWrapLayout = (TextWrapLayout) findViewById(R.id.padded_right_textWrapLayout);
		paddedRightTextWrapLayout.setText(getText());
	}

	private String getText () {
		return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aut unde est hoc contritum "
			+ "vetustate proverbium: quicum in tenebris? Hoc loco tenere se Triarius non potuit. "
			+ "Nos paucis ad haec additis finem faciamus aliquando; Illa sunt similia: hebes acies "
			+ "est cuipiam oculorum, corpore alius senescit; Qua tu etiam inprudens utebare non "
			+ "numquam. Profectus in exilium Tubulus statim nec respondere ausus; Duo Reges: "
			+ "constructio interrete. Quae quidem sapientes sequuntur duce natura tamquam videntes; "
			+ "Bona autem corporis huic sunt, quod posterius posui, similiora. Videamus animi partes, "
			+ "quarum est conspectus illustrior; Ipse Epicurus fortasse redderet, ut Sextus "
			+ "Peducaeus, Sex. At iste non dolendi status non vocatur voluptas. Sed quanta sit alias, "
			+ "nunc tantum possitne esse tanta. Age sane, inquam. Sed ille, ut dixi, vitiose";
	}

}
