/*
 * Copyright (C) 2011 Roque Pinel
 *
 * This file is part of SetFundao.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 */

package br.repinel.setfundao.ui.tab;

import br.repinel.setfundao.R;
import android.os.Bundle;

/**
 * Fourth tab.
 * 
 * @author Roque Pinel
 *
 */
public class Tab4Activity extends CameraTabActivity {

	/**
	 * @see br.repinel.setfundao.ui.tab.CameraTabActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		photoURL = getResources().getStringArray(R.array.urls)[3];

		super.onCreate(savedInstanceState);
	}
}