# Term
term 查询可以用于精确匹配一个字段中的某个值,不会对输入做任何处理,也不会进行大小写转换。
# Match
match查询会先对搜索词进行分词,分词完毕后再逐个对分词结果进行匹配，因此相比于term的精确搜索，
match是分词匹配搜索,match搜索还有两个相似功能的变种，一个是match_phrase，一个是multi_match。