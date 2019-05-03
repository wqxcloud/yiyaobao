package com.yiyaobao.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yiyaobao.R;

import java.util.ArrayList;

public class LetterView extends ViewGroup {
	private Context mContext;
	private CharacterClickListener mListener;

	//自定义属性(item的背景默认透明)
	private int rootBgColor;
	private int rootTouchBgColor;
	private int itemTouchBgColor;
	private int itemTextColor;
	private int itemTextTouchBgColor;
	private int itemTextSize;

	//记录上一次touch的位置
	private int mOldViewIndex;
	private int mTempIndex;
	//root的宽高
	private int mWidth;
	private int mHeight;
	//需要添加进来的item给定的高度
	private int mItemHeight;

	//root上边和下边的内间距
	private int marginTop = 5;//letterView距离顶部位置(systemui+toolbar)
	private int marginBottom = 5;

	//touch move最小距离
	private int mTouchSlop;
	//记录touch的down与move位置，以便计算移动的距离
	private int yDown;
	private int yMove;

	int mCharacterSize = 0;
	ArrayList<String> mCharacterList;

	public void setCharacterListener(CharacterClickListener listener) {
		mListener = listener;
	}

	public interface CharacterClickListener {
		void clickCharacter(String character);
		void showTip(int position, String content, boolean isShow);
		void clickArrow();
	}

	public LetterView(Context context) {
		this(context, null);
	}

	public LetterView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LetterView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		//setOrientation(VERTICAL);
		
		init(context, attrs);
		initView();
		setData(mCharacterList);
	}
	private void initView() {
		//addView(buildImageLayout());

		mCharacterList = new ArrayList<String>();
		mCharacterList.add("#");
		for (char i = 'A'; i <= 'Z'; i++) {
			final String character = String.valueOf(i);
			//TextView tv = buildTextLayout(character);

			//addView(tv);
			mCharacterSize++ ;
			mCharacterList.add(character); 
		}
		//addView(buildTextLayout("#"));

		mCharacterSize = mCharacterSize + 1;
	}

public ArrayList<String> getCharacterList(){
	return mCharacterList;
}
	public void setData(ArrayList<String> list) {
		if (list == null || list.size() <= 0)
			return;
		int size = list.size();
		list.addAll(list);
		for (int i = 0; i < size; i++) {
			addView(list.get(i), i);
		}
		//requestLayout();//重新measure和layout
		//invalidate();//重新draw
		//postInvalidate();//重新draw
	}

	private void addView(String firstPinYin, int position) {
		TextView textView = new TextView(mContext);
		textView.setText(firstPinYin);
		textView.setBackgroundColor(Color.TRANSPARENT);
		textView.setTextColor(itemTextColor);
		textView.setTextSize(itemTextSize);
		textView.setGravity(Gravity.CENTER);
		textView.setTag(position);
		addView(textView, position);
	}
	private void init(Context context, AttributeSet attrs) {
		//获取自定义属性

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterView);
		int count = typedArray.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.LetterView_rootBgColor:
				//容器的背景颜色，没有则使用指定的默认值
				rootBgColor = typedArray.getColor(attr, Color.parseColor("#80808080"));
				break;
			case R.styleable.LetterView_rootTouchBgColor:
				//容器touch时的背景颜色
				rootTouchBgColor = typedArray.getColor(attr, Color.parseColor("#EE808080"));
				break;
			case R.styleable.LetterView_letterTouchBgColor:
				//item项的touch时背景颜色(item的背景默认透明)
				itemTouchBgColor = typedArray.getColor(attr, Color.parseColor("#000000"));
				break;
			case R.styleable.LetterView_letterTextColor:
				//item的文本颜色
				itemTextColor = typedArray.getColor(attr, Color.parseColor("#FFFFFF"));
				break;
			case R.styleable.LetterView_letterTextTouchBgColor:
				//item在touch时的文本颜色
				itemTextTouchBgColor = typedArray.getColor(attr, Color.parseColor("#FF0000"));
				break;
			case R.styleable.LetterView_letterTextSize:
				//item的文本字体(默认16)
				itemTextSize = typedArray.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
				break;
			}
		}
		//回收属性数组
		typedArray.recycle();
		this.mContext = context;
		//设置容器默认背景
		setBackgroundColor(rootBgColor);
		//获取系统指定的最小move距离
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	/**
	 * 测量子view和自己的大小
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//获取系统测量的参数
		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		mHeight = MeasureSpec.getSize(heightMeasureSpec);
		//第一个元素的top位置
		int top = 5;//第一个元素距离 顶部的位置(systemui+toolbar)
		//item个数
		int size = 0;
		//LogUtils.i(TAG, "mCharacterSize="+mCharacterSize);
		if (mCharacterSize > 0) {
			//获取子孩子个数
			size = mCharacterSize;
			//上下各减去5px，除以个数计算出每个item的应有height
			mItemHeight = (mHeight - marginTop - marginBottom) / size;
		}
		//LogUtils.i(TAG, "mItemHeight="+mItemHeight);
		/**
		 * 此循环只是测量计算textview的上下左右位置数值，保存在其layoutParams中
		 */
		//LogUtils.i(TAG, "size="+size);
		for (int i = 0; i < size; i++) {
			TextView textView = (TextView) getChildAt(i);
			LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
			layoutParams.height = mItemHeight;//每个item指定应有的高度
			layoutParams.width = mWidth;//宽度为容器宽度
			//LogUtils.i(TAG, "layoutParams.top="+layoutParams.top);
			//LogUtils.i(TAG, "top="+top);
			layoutParams.top =  top;//第一个item距上边5px
			top += mItemHeight;//往后每个item距上边+mItemHeight距离
		}
		/**
		 * 由于此例特殊，宽度指定固定值，高度也是占满屏幕，不存在wrap_content情况
		 * 故不需要根据子孩子的宽高来改动 mWidth 和 mHeight 的值。故最后直接保存初始计算的值
		 */
		setMeasuredDimension(mWidth, mHeight);
	}

	/**
	 * 根据计算的子孩子值，在容器中布局排版子孩子
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//获取子孩子个数
		int size = getChildCount();
		for (int i = 0; i < size; i++) {
			//得到特性顺序的子孩子
			TextView textView = (TextView) getChildAt(i);
			//拿到孩子中保存的数据
			LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
			textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mItemHeight - 5);
			//给子孩子布局位置(左，上，右，下)
			textView.layout(0, layoutParams.top, layoutParams.width, layoutParams.top + layoutParams.height);
		}
		/**
		 * 结束了 onMeasure 和 onLayout 之后，当前容器的职责完成，onDraw 由子孩子自己画
		 */
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//当前手指位置的y坐标
		int y = (int) event.getY();
		//根据当前的y计算当前所在child的索引位置
		int tempIndex = computeViewIndexByY(y);
		if (tempIndex != -1) {
			mTempIndex = tempIndex;
			String textContent = ((TextView) getChildAt(tempIndex)).getText().toString().trim();
			//两头我留了点距离，不等于-1就代表没出界
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				yDown = y;
				drawTextView(mOldViewIndex, false);
				drawTextView(tempIndex, true);
				mOldViewIndex = tempIndex;
				if (mListener != null) {
					mListener.showTip(tempIndex,textContent,true);
				}
				//设置root touch bg
				setBackgroundColor(rootTouchBgColor);
				break;
			case MotionEvent.ACTION_MOVE:
				yMove = y;
				int distance = yDown - yMove;
				if (Math.abs(distance) > mTouchSlop) {
					//移动距离超出了一定范围
					if (mOldViewIndex != tempIndex) {
						//移动超出了当前元素
						drawTextView(mOldViewIndex, false);
						drawTextView(tempIndex, true);
						mOldViewIndex = tempIndex;
						setBackgroundColor(rootTouchBgColor);
						if (mListener != null) {
							mListener.showTip(tempIndex, textContent, true);
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				drawTextView(mOldViewIndex, false);
				drawTextView(tempIndex, false);
				mOldViewIndex = tempIndex;
				setBackgroundColor(rootBgColor);
				if (mListener != null) {
					mListener.showTip(tempIndex, textContent, false);
				}
				break;
			}
		} else {
			//出界了，可能是上边或者下边出界，恢复上边的元素
			if (mCharacterSize > 0) {
				drawTextView(mOldViewIndex, false);
				setBackgroundColor(rootBgColor);
				if (mListener != null) {
					mListener.showTip(mOldViewIndex, ((TextView) getChildAt(mOldViewIndex)).getText().toString().trim(), false);
				}
			}
		}
		return true;
	}

	/**
	 * 修改右边索引处touch的样式
	 *
	 * @param index       position
	 * @param isDrawStyle 是否设置特有的色彩样式
	 */
	private void drawTextView(int index, boolean isDrawStyle) {
		if (index < 0 || index >= mCharacterSize){
			return;
		}
		TextView textView = (TextView) getChildAt(index);
		if (textView == null){
			return;
		}
		if (isDrawStyle) {
			textView.setBackgroundColor(itemTouchBgColor);
			textView.setTextColor(itemTextTouchBgColor);
		} else {
			textView.setBackgroundColor(Color.TRANSPARENT);
			textView.setTextColor(itemTextColor);
		}
	}

	public void revertTextColor(){
		if (mOldViewIndex < 0 || mOldViewIndex >= mCharacterSize){
			return;
		}
		TextView textView = (TextView) getChildAt(mOldViewIndex);
		if (textView == null){
			return;
		}
		textView.setBackgroundColor(Color.TRANSPARENT);
		textView.setTextColor(itemTextColor);
		setBackgroundColor(rootBgColor);
	}
	/**
	 * 依据y坐标、子孩子的高度和容器总高度计算当前textview的索引值
	 */
	private int computeViewIndexByY(int y) {
		int returnValue;
		if (y < marginTop || y > (marginTop + mItemHeight * mCharacterSize)) {
			returnValue = -1;
		} else {
			int times = (y - marginTop) / mItemHeight;
			int remainder = (y - marginTop) % mItemHeight;
			if (remainder == 0) {
				returnValue = --times;
			} else {
				returnValue = times;
			}
		}
		return returnValue;
	}

	private TextView buildTextLayout(final String character) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);

		TextView tv = new TextView(mContext);
		tv.setLayoutParams(layoutParams);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(18/mContext.getResources().getDisplayMetrics().scaledDensity);
		//tv.setClickable(true);

		tv.setText(character);

		/*tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.clickCharacter(character);
                }
            }
        });*/

		return tv;
	}


	private ImageView buildImageLayout() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);

		ImageView iv = new ImageView(mContext);
		iv.setLayoutParams(layoutParams);

		//iv.setBackgroundResource(R.mipmap.arrow);

		/*iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.clickArrow();
                }
            }
        });*/
		return iv;
	}







	/**
	 * 必须重写的方法
	 *
	 * @return
	 */
	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	public static class LayoutParams extends ViewGroup.LayoutParams {
		public int left;
		public int top;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}
	}


}
