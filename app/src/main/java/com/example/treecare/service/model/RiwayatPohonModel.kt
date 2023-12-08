package com.example.treecare.service.model

class RiwayatPohonModel(
    var id: String? = null,
    var identitasPohon: IdentitasPohonModel? = null,
    var user: UserModel? = null,
    var keliling: Float? = null,
    var tinggi: Float? = null,
    var lebarTajuk: Float? = null,
    var bentuk: String? = null,
    var liveCrownRatio: Int? = null,
    var sejarahPemangkasan: String? = null,
    var warnaDaun: String? = null,
    var epicormic: Boolean? = null,
    var kerapatanDaun: String? = null,
    var ukuranDaun: String? = null,
    var woundWood: String? = null,
    var twigDieback: Boolean? = null,
    var vigor: String? = null,
    var hama: String? = null,
    var karakteristikTapak: String? = null,
    var gangguan: Boolean? = null,
    var masalahTanah: String? = null,
    var gangguanLain: String? = null,
    var pemanfaatanSekitar: String? = null,
    var dapatDipindahkan: Boolean? = null,
    var dapatDibatasi: Boolean? = null,
    var hunian: String? = null,
    var riwayatKerusakanPohon: List<RiwayatKerusakanPohonModel>? = null,
    var tanggal: String? = null,
    var jam: String? = null
)
//     constructor()
//     constructor(
//         id:                     String?,
//         identitas_pohon_id:     String?,
//         user_id:                String?,
//         keliling:               Float?,
//         tinggi:                 Float?,
//         lebar_tajuk:            Float?,
//         bentuk:                 String?,
//         live_crown_ratio:       Int?,
//         sejarah_pemangkasan:    String?,
//         warna_daun:             String?,
//         epicormic:              Boolean?,
//         kerapatan_daun:         String?,
//         ukuran_daun:            String?,
//         wound_wood:             String?,
//         twig_dieback:           Boolean?,
//         vigor:                  String?,
//         hama:                   String?,
//         karakteristik_tapak:    String?,
//         gangguan:               Boolean?,
//         masalah_tanah:          String?,
//         gangguan_lain:          String?,
//         pemanfaatan_sekitar:    String?,
//         dapat_dipindahkan:      Boolean?,
//         dapat_dibatasi:         Boolean?,
//         hunian:                 Boolean?,   
//     ) {
//         this.id = id                     
//         this.identitas_pohon_id = identitas_pohon_id     
//         this.user_id    = user_id                
//         this.keliling = keliling               
//         this.tinggi = tinggi                 
//         this.lebar_tajuk = lebar_tajuk            
//         this.bentuk = bentuk                 
//         this.live_crown_ratio = live_crown_ratio       
//         this.sejarah_pemangkasan = sejarah_pemangkasan    
//         this.warna_daun = warna_daun             
//         this.epicormic = epicormic              
//         this.kerapatan_daun = kerapatan_daun         
//         this.ukuran_daun = ukuran_daun            
//         this.wound_wood = wound_wood             
//         this.twig_dieback = twig_dieback           
//         this.vigor = vigor                  
//         this.hama = hama                   
//         this.karakteristik_tapak = karakteristik_tapak    
//         this.gangguan = gangguan               
//         this.masalah_tanah = masalah_tanah          
//         this.gangguan_lain = gangguan_lain          
//         this.pemanfaatan_sekitar = pemanfaatan_sekitar    
//         this.dapat_dipindahkan = dapat_dipindahkan
//         this.dapat_dibatasi = dapat_dibatasi   
//         this.hunian = hunian           
//     }
// }