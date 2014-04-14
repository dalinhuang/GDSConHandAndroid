package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MovieTheatre3DActivity extends Activity {
	int[] imageIds = new int[]
	{
		R.drawable.movie_xiyouxiangmo, R.drawable.movie_hobbit,
		R.drawable.movie_lifttohell, R.drawable.movie_skyfall };
	String[] imageNames = new String[]
	{
		"《西游·降魔篇》讲述的是一个妖魔横行的世界，百姓苦不堪言。年轻的驱魔人玄奘以“舍小我，成大我”的大无畏精神，历尽艰难险阻，依次收服猪妖以及妖王之王孙悟空为徒，并用“大爱”将他们感化，而玄奘自己也终于领悟到了“大爱”的真意。为救天下苍生于水火，为赎还自己的罪恶，师徒四人义无反顾的踏上西行取经之路。",
		"《霍比特人》的故事大致发生在《魔戒》三部曲之前60年左右，讲述弗罗多的叔叔——“霍比特人”比尔博·巴金斯（马丁·弗瑞曼 饰）的冒险历程。他被卷入了一场收回矮人的藏宝地孤山的旅程——这个地方被恶龙史矛革所占领着。由于灰袍巫师甘道夫（伊安·麦克莱恩 饰），比尔博出乎意料地加入了由13个矮人组成的冒险队伍中。他们要面对成千上万的哥布林、半兽人，致命的座狼骑士以及巨大的蜘蛛怪，变形者以及巫师……",
		"《电梯惊魂》神秘的半岛医院，守门人胡师傅的自杀像多米诺骨牌倒下的第一块，平静的医院自此陷入恐怖旋涡。先是马护士收到没有落款的字条，上面写着：“你也有今天！”不久，马护士的尸体在地下室被人发现，死因是受惊过度。可是，谁也不知道，马护士死之前竟被电梯带到了地下18层，而医院电梯本来只有地下2层！",
		"《007 天幕危机》詹姆斯·邦德(丹尼尔·克雷格饰)在伊斯坦堡的任务失败后而失去踪影，外界推测他已身亡，北约卧底探员资料竟因而外泄，身为邦德上司的M夫人(朱迪·丹奇饰)因此受到情报安全委员会新主席马洛利(拉尔夫·费因斯饰)的强烈质疑，遂成为政府调查的对象。而总部MI6竟遭攻击，被众人以为殉职的邦德秘密现身协助M夫人，她要邦德追查一名极度危险的罪犯，于是他循着线索前往澳门与上海。在神秘女子赛菲茵(贝纳尼丝·玛尔洛饰)与探员伊芙(娜奥米·哈里斯饰)的协助下，邦德追踪到在背后搞鬼的神秘人物洛乌西法(哈维尔·巴登饰)，更意外发现M夫人不为人知的秘密。为了拯救总部，邦德是否能再次不顾一切出面化解危机？"
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_theatre_3d);
		final Gallery gallery = (Gallery) findViewById(R.id.gallery);

		// 创建一个BaseAdapter对象，该对象负责提供Gallery所显示的图片
		BaseAdapter adapter = new BaseAdapter()
		{
			@Override
			public int getCount()
			{
				return imageIds.length;
			}
			@Override
			public Object getItem(int position)
			{
				return position;
			}
			@Override
			public long getItemId(int position)
			{
				return position;
			}

			// 该方法的返回的View就是代表了每个列表项
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				// 创建一个ImageView
				ImageView imageView = new ImageView(MovieTheatre3DActivity.this);
				imageView
					.setImageResource(imageIds[position % imageIds.length]);
				// 设置ImageView的缩放类型
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new Gallery.LayoutParams(150, 200));
				TypedArray typedArray = obtainStyledAttributes(
					R.styleable.Gallery);
				imageView.setBackgroundResource(typedArray.getResourceId(
					R.styleable.Gallery_android_galleryItemBackground, 0));
				return imageView;
			}
		};
		gallery.setAdapter(adapter);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			// 当Gallery选中项发生改变时触发该方法
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id)
			{
				TextView filmNameView = (TextView) findViewById(R.id.text_film_name);
				filmNameView.setText(imageNames[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}
}
