/*
 * Copyright 2008 Novamente LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package relex.entity;
import relex.feature.FeatureNode;

public class EmoticonEntityInfo extends EntityInfo {

	public EmoticonEntityInfo(String _originalSentence, int _firstCharIndex, int _lastCharIndex) {
		super(_originalSentence, _firstCharIndex, _lastCharIndex);
	}

	public String idStringPrefix() {
		return "emoticonID";
	}

	protected void setProperties(FeatureNode fn)
	{
		super.setProperties(fn);
		fn.set("EMOTICON-FLAG", new FeatureNode("T"));
	}
}