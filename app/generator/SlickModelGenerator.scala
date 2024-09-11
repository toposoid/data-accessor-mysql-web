package generator

import com.ideal.linked.common.DeploymentConverter.conf
import slick.codegen.SourceCodeGenerator

import java.io.{File, PrintWriter}
import scala.io.Source

object SlickModelGenerator extends App {
  SourceCodeGenerator.main(
    Array(
      "profiles.MySQLProfile", //DBドライバー。古い情報だと  "slick.driver.PostgresDriver"になっていることが結構あるが、これはslick3.2.0で変わってしまった。slickと一緒でおけ
      "com.mysql.cj.jdbc.Driver", //JDBCのDBドライバー？まあslickと一緒でおけ
      "jdbc:mysql://"+ conf.getString("TOPOSOID_RDB_HOST") + "/toposoiddb?serverTimezone=UTC&useSSL=false", //DBのurl。ここもslickと一緒でおけ
      "/tmp/", // コードの出力先ディレクトリ。
      "model", //  上と合わせてこう書くとapp/infrastracture/dto/Tables.scalaというパスで１ファイルに全て書かれる。基本Slickは１ファイルに全てが書かれる形だが、一番最後のオプションをtrueにすると分割して作成できる。
      "root", // dbのユーザー名。slickと一緒でおけ
      "p@ssw0rd", // dbのパスワード。slickと一緒でおけ,
      "true", // ignoreInvalidDefaultsオプションらしい。よくわからない。
      "slick.codegen.SourceCodeGenerator", // codeGeneratorClassオプション。これはデフォルトはこうなってるのでこのままでいい。
      "true" // outputToMultipleFilesオプション。デフォルトはfalseだが、これをtrueにするとファイルが分割されて出力される。
    )
  )


  val dir = new File("/tmp/model/").getAbsoluteFile()
  dir.list().foreach(x => {
    val convertFile = new File("/tmp/dummy")
    val w = new PrintWriter(convertFile)
    val generatedFile = "/tmp/model/" + x
    Source.fromFile(generatedFile).getLines
      .map { y => y.replace("package model", "package model\nimport java.time.LocalDateTime").replace("java.sql.Timestamp", "LocalDateTime")}
      .foreach(x => w.println(x))
    w.close()
    convertFile.renameTo(new File("app/generator/model/" + new File(generatedFile).getName))
  })


}
