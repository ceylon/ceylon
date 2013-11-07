"Represents the range of totally ordered, ordinal values 
 generated by two endpoints of type `Ordinal` and 
 `Comparable`. If the first value is smaller than the
 last value, the range is increasing. If the first value
 is larger than the last value, the range is decreasing.
 If the two values are equal, the range contains exactly
 one element. The range is always nonempty, containing 
 at least one value.
 
 A range may be produced using the `..` operator:
 
     for (i in min..max) { ... }
     if (char in 'A'..'Z') { ... }
 "
by ("Gavin")
shared final class Range<Element>(first, last) 
        extends Object() 
        satisfies [Element+] & 
                  Cloneable<Range<Element>>
        given Element satisfies Ordinal<Element> & 
                                Comparable<Element> { 
    
    "The start of the range."
    shared actual Element first;
    
    "The end of the range."
    shared actual Element last;
    
    shared actual String string => 
            first.string + ".." + last.string;
    
    "Determines if the range is decreasing."
    shared Boolean decreasing => last<first; 
    
    Element next(Element x) =>
            decreasing then x.predecessor 
                    else x.successor;

    "The nonzero number of elements in the range."
    shared actual Integer size {
    	if (is Enumerable<Anything> last, 
    	    is Enumerable<Anything> first) {
    		return (last.integerValue - 
    		        first.integerValue)
    		            .magnitude+1;
    	}
    	else {
    		variable Integer size = 1;
    		variable Element current=first;
    		while (current!=last) {
    			size++;
    			current = next(current);
    		}
            return size;
    	}
    }
    
    "The index of the end of the range."
    shared actual Integer lastIndex => size-1; 
    
    "The rest of the range, without the start of the
     range."
    shared actual Element[] rest {
        if (size==1) { return {}; }
        Element n = next(first);
        return n==last then {} else Range<Element>(n,last);
    }
    
    "The element of the range that occurs `n` values after
     the start of the range. Note that this operation 
     is inefficient for large ranges."
    shared actual Element? get(Integer n) {
        //optimize this for numbers!
        variable Integer index=0;
        variable Element x=first;
        while (index<n) {
            if (x==last) {
                return null;
            }
            else {
                ++index;
                x=next(x);
            }
        }
        return x;
    }
    
    "An iterator for the elements of the range."
    shared actual Iterator<Element> iterator() {
        class RangeIterator()
                satisfies Iterator<Element> {
            variable Element|Finished current = first;
            shared actual Element|Finished next() {
                Element|Finished result = current;
                if (!is Finished curr = current) {
                    if (decreasing then curr<=last else curr>=last) {
                        current = finished;
                    } 
                    else {
                        current = outer.next(curr);
                    }
                }
                return result;
            }
            shared actual String string {
                return "RangeIterator";
            }
        }
        return RangeIterator();
    }
    
    shared actual {Element+} by(Integer step) {
        "step size must be greater than zero"
        assert (step > 0);
        if (step == 1) {
            return this;
        }
        if (is Integer first, is Integer last) {
            return integerRangeByIterable(this, step);
        }
        //assert (is {Element+} result = super.by(step));
        //return result;
        return super.by(step);
    }
    
    shared actual Integer count(Boolean selecting(Element element)) {
        variable value e = first;
        variable value c = 0;
        while (containsElement(e)) {
            if (selecting(e)) {
                c++;
            }
            e = next(e);
        }
        return c;
    }
    
    "Determines if this range includes the given object."
    shared actual Boolean contains(Object element) {
        if (is Element element) {
            return containsElement(element);
        }
        else {
            return false;
        }
    }
    
    "Determines if this range includes the given value."
    shared actual Boolean occurs(Anything element) {
        if (is Element element) {
            return containsElement(element);
        }
        else {
            return false;
        }
    }
    
    "Determines if the range includes the given value."
    shared Boolean containsElement(Element x) =>
            decreasing then x<=first && x>=last
                    else x>=first && x<=last;
    
    shared actual Boolean includes(List<Anything> sublist) {
        if (is Range<Element> sublist) {
            return includesRange(sublist);
        }
        else {
            return super.includes(sublist);
        }
    }
    
    "Determines if this range includes the given range."
    shared Boolean includesRange(Range<Element> sublist) {
        return first<=sublist.first<=last &&
                first<=sublist.last<=last;
    }
    
    "Determines if two ranges are the same by comparing
     their endpoints."
    shared actual Boolean equals(Object that) {
        if (is Range<Object> that) {
            //optimize for another Range
            return that.first==first && that.last==last;
        }
        else {
            //it might be another sort of List
            return super.equals(that);
        }
    }
    
    "Returns the range itself, since ranges are 
     immutable."
    shared actual Range<Element> clone => this;
    
    shared actual Range<Element>|Empty segment(
            Integer from, 
            Integer length) {
        if (length<=0 || from>lastIndex) {
            return {};
        }
        variable value x=first;
        variable value i=0;
        while (i++<from) { x=next(x); }
        variable value y=x;
        variable value j=1;
        while (j++<length && y<last) { y=next(y); }
        return Range<Element>(x, y);
    }
    
    shared actual Range<Element>|Empty span(
            Integer from, Integer to) {
        variable value toIndex=to;
        variable value fromIndex=from;
        if (toIndex<0) {
            if (fromIndex<0) {
                return {};
            }
            toIndex=0;
        }
        else if (toIndex>lastIndex) {
            if (fromIndex>lastIndex) {
                return {};
            }
            toIndex=lastIndex;
        }
        if (fromIndex<0) {
            fromIndex=0;
        }
        else if (fromIndex>lastIndex) {
            fromIndex=lastIndex;
        }
        variable value x=first;
        variable value i=0;
        while (i++<fromIndex) { x=next(x); }
        variable value y=first;
        variable value j=0;
        while (j++<toIndex) { y=next(y); }
        return Range<Element>(x, y);
    }
    
    shared actual Range<Element>|Empty spanTo(Integer to) {
        return to < 0 then {} else span(0, to);
    }
    
    shared actual Range<Element>|Empty spanFrom(Integer from) {
        return span(from, size);
    }

    "Reverse this range, returning a new range."
    shared actual Range<Element> reversed => Range(last,first);
    
    shared actual Range<Element>|Empty skipping(Integer skip) {
        variable value x=0;
        variable value e = first;
        while (x++<skip) {
            e=next(e);
        }
        return containsElement(e) then Range(e, last) else {};
    }
    
    shared actual Range<Element>|Empty taking(Integer take) {
        if (take == 0) {
            return {};
        }
        variable value x=0;
        variable value e=first;
        while (++x<take) {
            e=next(e);
        }
        return containsElement(e) then Range(first, e) else this;
    }

    "Returns the range itself, since a Range cannot
     contain nulls."
    shared actual Range<Element> coalesced => this;
    
    "Returns this range."
    shared actual Range<Element> sequence => this;
    
}
