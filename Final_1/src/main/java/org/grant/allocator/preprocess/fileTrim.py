from lxml import etree
import os


def remove(file_path, output_file):
    """预处理XML文件，移除除author和year之外的所有子元素"""
    with open(output_file, 'wb') as out_file:
        out_file.write(b'<?xml version="1.0" encoding="UTF-8"?>\n')
        out_file.write(b'<dblp>\n')  # 假设根元素是'dblp'

        for event, elem in etree.iterparse(file_path, events=('end',), tag='*', recover=True):
            if 'key' in elem.attrib:
                new_elem = etree.Element(elem.tag, attrib=elem.attrib)
                # 仅保留author和year子元素
                for child in elem:
                    if child.tag in ['author', 'year']:
                        new_child = etree.SubElement(new_elem, child.tag)
                        new_child.text = child.text

                record = etree.tostring(new_elem, encoding='utf-8', pretty_print=True)
                out_file.write(record)

            # # 清除元素以节省内存
            # elem.clear()
            # while elem.getprevious() is not None:
            #     del elem.getparent()[0]

        out_file.write(b'</dblp>\n')


# 替换您的文件路径和输出路径
remove('C:\\Works\\大三\\分布式系统\\DS_Final\\dblp.xml', 'C:\\Works\\大三\\分布式系统\\DS_Final\\split_files\\dblp_trim.xml')


