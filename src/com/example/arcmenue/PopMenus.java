package com.example.arcmenue;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class PopMenus extends ViewGroup implements OnClickListener{
	public static final int POS_LIFT_TOP=0;
	public static final int POS_LIFT_BOTTOM=1;
	public static final int POS_RIGHT_TOP=2;
	public static final int POS_RIGHT_BOTTM=3;
	//按钮
	private View mButton;
	//默认的位置
	private Postions mpostion=Postions.LIFT_TOP;
	//默认的状态
	private Status mStatus=Status.CLOSE;
	//半径
	private int mRadius;
	//枚举出位置
	private enum Postions{
		LIFT_TOP,LIFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM
	}
	//判断按钮的状态
	private enum Status{
		OPEN,CLOSE;
	}
	//初始化这个接口
	private OnMenuItemListener itemListener;
	//对外部公布出去;
	public void setOnMenuItemListener(OnMenuItemListener itemListener){
		this.itemListener=itemListener;
	}
	//接口，用于监听点击这个item 的点击事件
	public interface OnMenuItemListener{
		void onClick(View v,int pos);
	}
	public PopMenus(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mRadius=setdp2Px(100);
		//获取自定义的xml
		TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.ArcMenu);
		int pos=array.getInt(R.styleable.ArcMenu_postion,POS_LIFT_TOP );
		switch (pos) {
		case POS_LIFT_TOP:
			mpostion=Postions.LIFT_TOP;
			break;
		case POS_LIFT_BOTTOM:
			mpostion=Postions.LIFT_BOTTOM;
			break;
		case POS_RIGHT_TOP:
			mpostion=Postions.RIGHT_TOP;
			break;
		case POS_RIGHT_BOTTM:
			mpostion=Postions.RIGHT_BOTTOM;
			break;
		default:
			break;
		}
		mRadius=(int) array.getDimension(R.styleable.ArcMenu_radues, setdp2Px(100));
		Log.v("TAG", mpostion+"postion"+mRadius+"r");
		array.recycle();
	}
//测量
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//获取所以得子view的数量
		int count=getChildCount();
		//然后遍历所以得子view
		for (int i = 0; i < count; i++) {
			View childView=getChildAt(i);
			//然后对每一个view进行测量
			measureChild(childView, widthMeasureSpec, heightMeasureSpec);
		}
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		//然后在onlayout中进行布局
		if(changed){
			layoutButton();
			//按钮以外的其他item
			layoutItem();
		}
	}
	private void layoutItem() {
		// TODO Auto-generated method stub
		//获取其总的item,
		int count=getChildCount();
		//遍历，count-1是指除了按钮之外的其他item
		for (int i = 0; i < count-1; i++) {
			//然后是0的角标是取不到的，因为0角标是按钮的位置
			View childView=getChildAt(i+1);
			childView.setVisibility(View.GONE);
			int width=childView.getMeasuredWidth();
			int heiht=childView.getMeasuredHeight();
			int l=(int) (mRadius*Math.sin(Math.PI/2/(count-2)*i));
			int t=(int) (mRadius*Math.cos(Math.PI/2/(count-2)*i));
			if(mpostion==Postions.LIFT_BOTTOM||mpostion==Postions.RIGHT_BOTTOM){
				t=getMeasuredHeight()-t-heiht;
			}if(mpostion==Postions.RIGHT_TOP||mpostion==Postions.RIGHT_BOTTOM){
				l=getMeasuredWidth()-l-width;
			}
			childView.layout(l, t, l+width, t+heiht);
		}
	}
	private void layoutButton() {
		// TODO Auto-generated method stub
		mButton=getChildAt(0);
		mButton.setOnClickListener(this);
		int width=mButton.getMeasuredWidth();
		int height=mButton.getMeasuredHeight();
		int t=0;
		int l=0;
		switch (mpostion) {
		case LIFT_TOP:
			t=0;
			l=0;
			break;
		case LIFT_BOTTOM:
			l=0;
			t=getMeasuredHeight()-height;
			break;
		case RIGHT_TOP:
			t=0;
			l=getMeasuredWidth()-width;
			break;
		case RIGHT_BOTTOM:
			t=getMeasuredHeight()-height;
			l=getMeasuredWidth()-width;
		break;
		default:
			break;
		}
		mButton.layout(l, t, l+width, t+height);
	}
	//将dp-px
	private int setdp2Px(int dp){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//点击按钮的旋转动画
		RotateButtonAnim(v,300);
		toggleMenu(300);
		
	}
	public void toggleMenu(int duration) {
		// TODO Auto-generated method stub
		int count=getChildCount();
		for (int i = 0; i < count-1; i++) {
			final View childView=getChildAt(i+1);
			childView.setVisibility(View.VISIBLE);
			int l=(int) (mRadius*Math.sin(Math.PI/2/(count-2)*i));
			int t=(int) (mRadius*Math.cos(Math.PI/2/(count-2)*i));
			
			//先进行平移动画
			int xflg=1;
			int yflg=1;
			//上面两个是对于y和x增加的时候考虑的，那就是在右下角的时候
			if(mpostion==Postions.LIFT_TOP||mpostion==Postions.LIFT_BOTTOM){
				xflg=-1;
			}if(mpostion==Postions.RIGHT_TOP||mpostion==Postions.LIFT_TOP){
				yflg=-1;
			}
			AnimationSet set=new AnimationSet(true);
			TranslateAnimation translateAnimation=null;
			//从展开状态到关闭状态的一个动画
			if(mStatus==Status.CLOSE){
				translateAnimation=new TranslateAnimation(l*xflg,0, t*yflg, 0);
			}else{
				translateAnimation=new TranslateAnimation(0,l*xflg, 0,t*yflg);
			}
			translateAnimation.setDuration(duration);
			translateAnimation.setFillAfter(true);
			//想像手指一样慢慢展开就设置一下每个开始的时间
			translateAnimation.setStartOffset(100*i/count);
			translateAnimation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					//动画结束时隐藏它；
					if(mStatus==Status.CLOSE){
						childView.setVisibility(View.GONE);
					}
				}
			});
			//加个旋转动画
			RotateAnimation rotateAnimation=new RotateAnimation(0, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setDuration(duration);
			rotateAnimation.setFillAfter(true);
			rotateAnimation.setDuration(duration);
			rotateAnimation.setFillAfter(true);
			set.addAnimation(rotateAnimation);
			set.addAnimation(translateAnimation);
			set.setDuration(duration);
			childView.startAnimation(set);
			final int pos=i+1;
			childView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.v("TAG", pos+"");
					//接口等下弄
					//点击这个先发大，然后在消失的动画，其他的都缩小消失
					if(itemListener!=null){
						itemListener.onClick(v, pos);
					}
					menuItemAnim(pos-1);
					changeStatus();
					
					
				}

				private void menuItemAnim(int pos) {
					// TODO Auto-generated method stub
					int count=getChildCount();
					for (int i = 0; i < count-1; i++) {
						View childView=getChildAt(i+1);
						if(i==pos){
							//说明选中了这一项
							Log.v("TAG", "选中了这一项");
							childView.startAnimation(scaleBigAnim(300));
						}else{
							childView.startAnimation(scaleSmalAnim(300));
						}
					}
				}

				private Animation scaleSmalAnim(int duration) {
					// TODO Auto-generated method stub
					AnimationSet set=new AnimationSet(true);
					ScaleAnimation animation=new ScaleAnimation(0.0f, 1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					AlphaAnimation alphaAnimation=new AlphaAnimation(0.0f, 1.0f);
					set.addAnimation(animation);
					set.addAnimation(alphaAnimation);
					set.setDuration(duration);
					return set;
				}

				private Animation scaleBigAnim(int duration) {
					// TODO Auto-generated method stub
					AnimationSet set=new AnimationSet(true);
					ScaleAnimation animation=new ScaleAnimation(1.0f, 4.0f,1.0f,4.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f, 0.0f);
					set.addAnimation(animation);
					set.addAnimation(alphaAnimation);
					set.setDuration(duration);
					return set;
				}
			});
			
		}
		//切换子菜单
		changeStatus();
	}
	private void changeStatus() {
		// TODO Auto-generated method stub
		//如果是从打开的状态到关闭的状态，就把状态改为打开，否则就关闭;
		mStatus=(mStatus==Status.CLOSE?Status.OPEN:Status.CLOSE);
	}
	private void RotateButtonAnim(View v,int duration) {
		// TODO Auto-generated method stub
		RotateAnimation rotateAnimation=new RotateAnimation(0, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(duration);
		rotateAnimation.setFillAfter(true);
		v.startAnimation(rotateAnimation);
	}
//公布一个方法改变状态
	public boolean isOpen(){
		return mStatus==Status.OPEN;
	}
}
