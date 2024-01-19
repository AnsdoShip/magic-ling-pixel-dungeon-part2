/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.noosa.ui;

import com.watabou.noosa.Group;

public class Component extends Group {

	public float x;
	public float y;
	protected float width;
	protected float height;

	public Component() {
		super();
		createChildren();
	}

	public Component setPos( float x, float y ) {
		this.x = x;
		this.y = y;
		layout();

		return this;
	}
	public final Component setX( float x ) { return setPos(x, y); }
	public final Component setY( float y ) { return setPos(x, y); }

	public Component setSize( float width, float height ) {
		this.width = width;
		this.height = height;
		layout();

		return this;
	}

	public final Component setHeight( float height ) { return setSize(width, height); }
	public final Component setWidth( float width ) 	 { return setSize(width, height); }

	public Component setRect( float x, float y, float width, float height ) {
		this.x = x;
		this.y = y;
		return setSize(width, height);
	}

	public boolean inside( float x, float y ) {
		return x >= this.x && y >= this.y && x < this.x + width && y < this.y + height;
	}

	public void fill( Component c ) {
		setRect( c.x, c.y, c.width, c.height );
	}

	public float left() {
		return x;
	}

	public float right() {
		return x + width;
	}

	public float centerX() {
		return x + width / 2;
	}

	public float top() {
		return y;
	}

	public float bottom() {
		return y + height;
	}

	public float centerY() {
		return y + height / 2;
	}

	public float width() {
		return width;
	}

	public float height() {
		return height;
	}

	protected void createChildren() {
	}

	protected void layout() {
	}
}