/*
 * Copyright (C) 2011 Roque
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

package br.repinel;

/**
 * Application Exception. 
 * 
 * @author Roque Pinel
 *
 */
public class MainException extends Exception {
	private static final long serialVersionUID = -2312138943929906420L;

	/**
	 * Creates an exception with a message.
	 * 
	 * @param message The message.
	 */
	public MainException(CharSequence message) {
		super(message.toString());
	}
}
