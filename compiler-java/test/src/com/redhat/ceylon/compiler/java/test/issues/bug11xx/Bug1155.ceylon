/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

@noanno
class Bug1155() {
    shared String narySequenced(
    String s1, String* s) 
        => "narySequenced(``s1``; ``s``)";
}
@noanno
void bug1155() {
    value b = Bug1155();
    String(String, String, String*) r = b.narySequenced;
    print(r("nary1", "rest1"));
    print(r("nary1", "rest1", "rest2"));
    
    String(String, String, String*)(Bug1155) staticRef = Bug1155.narySequenced;
    print(staticRef(b)("nary1", "rest1"));
    print(staticRef(b)("nary1", "rest1", "rest2"));
}