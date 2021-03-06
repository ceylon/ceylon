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
@nomodel
void keywordInForLoop(Sequence<Integer> seq) {
    @disableOptimization:"RangeOpIteration"
    for (Integer byte in 1..10) {
        if (byte > 5) {
            // Empty
        }
    }
    for(Integer byte in seq){
        if (byte > 5) {
            // Empty
        }
    }
    for(String byte in {"aap","noot","mies"}){
        // Empty
    }
    for(String? byte in {"aap",null,"mies"}){
        // Empty
    }
    for(Integer byte in {1,2,3}){
        // Empty
    }
    for(Integer? byte in {1,null,3}){
        // Empty
    }
    for(Character byte in "wim"){
        // Empty
    }
    for (Integer byte -> String long in {1->"a", 2->"b", 3->"c"}) {
        if (byte > 5) {
            // Empty
        }
    }
}
@noanno
shared Integer keywordInForLoop2(Sequence<Integer> seq){
    for(Integer byte in seq){
        while (true) {
            for(Integer long in seq){
                if (byte > long) {
                    break;
                }
            }
            else {
                return 1;
            }
            break;
        }
        break;
    }
    else {
        return 0;
    }
    return 2;
}
@noanno
class KeywordInForLoop3(shared Integer age) { 
    KeywordInForLoop3[] people = [KeywordInForLoop3(1), KeywordInForLoop3(18)];
    Boolean minors;
    for (byte in people) {
        if (byte.age<18) {
            minors = true;
            break;
        }
    }
    else {
        minors = false;
    }
}
